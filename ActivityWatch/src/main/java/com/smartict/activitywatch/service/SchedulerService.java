package com.smartict.activitywatch.service;

import com.smartict.activitywatch.entity.ApplicationActivity;
import com.smartict.activitywatch.entity.Usr;
import com.smartict.activitywatch.entity.UsrActivity;
import com.smartict.activitywatch.entity.WindowActivity;
import com.smartict.activitywatch.repository.ApplicationActivityRepository;
import com.smartict.activitywatch.repository.UsrActivityRepository;
import com.smartict.activitywatch.repository.UsrRepository;
import com.smartict.activitywatch.repository.WindowActivityRepository;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.sun.jna.platform.win32.WinUser.LASTINPUTINFO;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final UsrRepository usrRepository;
    private final WindowActivityRepository windowActivityRepository;
    private final ApplicationActivityRepository applicationActivityRepository;
    private final UsrActivityRepository usrActivityRepository;
    private final MainService mainService;

    private long initialTimeMillis;

    @PostConstruct
    public void init() {
        initialTimeMillis = System.currentTimeMillis();
    }

    @Scheduled(fixedRate = 60000)
    public void monitorActivities() {
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis - initialTimeMillis < 60000) {
            return;
        }

        try {
            String windowTitle = getActiveWindowTitle();
            String applicationName = getActiveApplicationName();
            String currentUsername = System.getProperty("user.name");

            if (windowTitle == null || applicationName == null) {
                log.warn("Window title or application name could not be found.");
                return;
            }

            Usr user = usrRepository.findByUsername(currentUsername)
                    .orElseGet(() -> {
                        Usr newUser = new Usr();
                        newUser.setUsername(currentUsername);
                        return usrRepository.save(newUser);
                    });

            WindowActivity windowActivity = getOrCreateWindowActivity(windowTitle);
            ApplicationActivity applicationActivity = getOrCreateApplicationActivity(applicationName);

            long idleTime = getIdleTimeMillis();

            log.info("Idle time: {}", idleTime);

            boolean isAfk = idleTime > 180000;

            saveUserActivity(user, windowActivity, applicationActivity, isAfk);

        } catch (Exception e) {
            log.error("Error while monitoring activities", e);
        }
    }

    private long getIdleTimeMillis() {
        LASTINPUTINFO info = new LASTINPUTINFO();
        info.cbSize = info.size();
        User32.INSTANCE.GetLastInputInfo(info);

        long lastInputTick = Integer.toUnsignedLong(info.dwTime);
        long currentTick = Kernel32.INSTANCE.GetTickCount64();

        return currentTick - lastInputTick;
    }

    private void saveUserActivity(Usr user, WindowActivity windowActivity, ApplicationActivity applicationActivity, boolean isAfk) {
        UsrActivity activity = new UsrActivity();
        activity.setUsr(user);
        activity.setWindowActivity(windowActivity);
        activity.setApplicationActivity(applicationActivity);
        activity.setDate(LocalDateTime.now());
        activity.setAfk(isAfk);
        usrActivityRepository.save(activity);
    }

    private WindowActivity getOrCreateWindowActivity(String title) {
        return windowActivityRepository.findByWindowTitle(title)
                .orElseGet(() -> {
                    WindowActivity wa = new WindowActivity();
                    wa.setWindowTitle(title);
                    return windowActivityRepository.save(wa);
                });
    }

    private ApplicationActivity getOrCreateApplicationActivity(String name) {
        return applicationActivityRepository.findByApplicationText(name)
                .orElseGet(() -> {
                    ApplicationActivity app = new ApplicationActivity();
                    app.setApplicationText(name);
                    return applicationActivityRepository.save(app);
                });
    }

    private String getActiveWindowTitle() {
        char[] buffer = new char[1024];
        WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.GetWindowText(hwnd, buffer, 1024);
        return NativeToString(buffer);
    }

    private String getActiveApplicationName() {
        WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        IntByReference pid = new IntByReference();
        User32.INSTANCE.GetWindowThreadProcessId(hwnd, pid);

        WinNT.HANDLE process = Kernel32.INSTANCE.OpenProcess(
                WinNT.PROCESS_QUERY_INFORMATION | WinNT.PROCESS_VM_READ,
                false,
                pid.getValue()
        );
        if (process == null) return null;

        byte[] exePath = new byte[1024];
        Psapi.INSTANCE.GetModuleBaseNameA(process, null, exePath, 1024);
        Kernel32.INSTANCE.CloseHandle(process);

        return NativeToString(exePath);
    }

    private String NativeToString(char[] buffer) {
        int len = 0;
        while (len < buffer.length && buffer[len] != 0) len++;
        return new String(buffer, 0, len);
    }

    private String NativeToString(byte[] buffer) {
        int len = 0;
        while (len < buffer.length && buffer[len] != 0) len++;
        return new String(buffer, 0, len, StandardCharsets.UTF_8);
    }

    public interface Psapi extends com.sun.jna.Library {
        Psapi INSTANCE = Native.load("Psapi", Psapi.class);
        int GetModuleBaseNameA(WinNT.HANDLE hProcess, WinDef.HMODULE hModule, byte[] lpBaseName, int nSize);
    }
}
