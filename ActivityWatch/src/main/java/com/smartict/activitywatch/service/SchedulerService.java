package com.smartict.activitywatch.service;

import com.smartict.activitywatch.entity.Activity;
import com.smartict.activitywatch.entity.TypeEnum;
import com.smartict.activitywatch.entity.Usr;
import com.smartict.activitywatch.entity.UsrActivity;
import com.smartict.activitywatch.repository.ActivityRepository;
import com.smartict.activitywatch.repository.UsrActivityRepository;
import com.smartict.activitywatch.repository.UsrRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final ActivityRepository activityRepository;
    private final UsrRepository usrRepository;
    private final UsrActivityRepository usrActivityRepository;

    private LocalDate lastRecordedDate = LocalDate.now();

    @Scheduled(fixedRate = 60000)
    public void monitorActivities() {
        try {
            checkDateAndResetIfNeeded();

            String activeWindowTitle = getActiveWindowTitle();
            String activeApplicationName = getActiveApplicationName();
            String currentUsername = System.getProperty("user.name");

            if (activeWindowTitle == null || activeApplicationName == null) {
                log.warn("Active window or application could not be determined.");
                return;
            }

            Optional<Usr> userOpt = usrRepository.findByUsername(currentUsername);

            Usr user;
            if (userOpt.isEmpty()) {
                user = new Usr();
                user.setUsername(currentUsername);
                user = usrRepository.save(user);
                log.info("New user created with username: {}", currentUsername);
            } else {
                user = userOpt.get();
            }

            Activity windowActivity = getOrCreateActivity(activeWindowTitle, TypeEnum.Window);
            Activity applicationActivity = getOrCreateActivity(activeApplicationName, TypeEnum.Application);

            saveUserActivity(user, windowActivity);
            saveUserActivity(user, applicationActivity);

            log.info("Activities logged successfully at {}", LocalDateTime.now());

        } catch (Exception e) {
            log.error("Error while monitoring activities", e);
        }
    }

    private void checkDateAndResetIfNeeded() {
        LocalDate today = LocalDate.now();
        if (!today.isEqual(lastRecordedDate)) {
            // Gün değişmiş demektir!
            log.info("New day detected. Resetting necessary fields at {}", LocalDateTime.now());

            resetDailyActivityData();

            lastRecordedDate = today;
        }
    }

    private void resetDailyActivityData() {

        usrActivityRepository.deleteAllByDateBefore(LocalDate.now().atStartOfDay());

        log.info("Daily activity data has been reset.");
    }

    private void saveUserActivity(Usr user, Activity activity) {
        UsrActivity userActivity = new UsrActivity();
        userActivity.setUsr(user);
        userActivity.setActivity(activity);
        userActivity.setDate(LocalDateTime.now());
        userActivity.setAfk(false);
        usrActivityRepository.save(userActivity);
    }

    private Activity getOrCreateActivity(String name, TypeEnum type) {
        return activityRepository.findByName(name)
                .orElseGet(() -> {
                    Activity newActivity = new Activity();
                    newActivity.setName(name);
                    newActivity.setType(type);
                    return activityRepository.save(newActivity);
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
        if (process == null) {
            return null;
        }

        byte[] exePath = new byte[1024];
        Psapi.INSTANCE.GetModuleBaseNameA(process, null, exePath, 1024);
        Kernel32.INSTANCE.CloseHandle(process);

        return NativeToString(exePath);
    }

    private String NativeToString(char[] buffer) {
        int len = 0;
        while (len < buffer.length && buffer[len] != 0) {
            len++;
        }
        return new String(buffer, 0, len);
    }

    private String NativeToString(byte[] buffer) {
        int len = 0;
        while (len < buffer.length && buffer[len] != 0) {
            len++;
        }
        return new String(buffer, 0, len, StandardCharsets.UTF_8);
    }

    public interface Psapi extends com.sun.jna.Library {
        Psapi INSTANCE = Native.load("Psapi", Psapi.class);

        int GetModuleBaseNameA(WinNT.HANDLE hProcess, WinDef.HMODULE hModule, byte[] lpBaseName, int nSize);
    }
}

