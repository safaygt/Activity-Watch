package com.smartict.activitywatch.service;

import com.smartict.activitywatch.dto.UsrActivityResponseDTO;
import com.smartict.activitywatch.entity.UsrActivity;
import com.smartict.activitywatch.repository.UsrActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {

    private final UsrActivityRepository usrActivityRepository;

    public List<UsrActivityResponseDTO> getAllActivities(LocalDate date) {
        List<UsrActivity> activities = usrActivityRepository.findAll()
                .stream()
                .filter(activity -> activity.getDate().toLocalDate().isEqual(date))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());

        return activities.stream().map(entity -> {
            UsrActivityResponseDTO dto = new UsrActivityResponseDTO();
            dto.setId(entity.getId());
            dto.setUsername(entity.getUsr().getUsername());
            dto.setWindowTitle(entity.getWindowActivity() != null ? entity.getWindowActivity().getWindowTitle() : null);
            dto.setApplicationName(entity.getApplicationActivity() != null ? entity.getApplicationActivity().getApplicationText() : null);
            dto.setDate(entity.getDate());
            dto.setAfk(entity.isAfk());
            return dto;
        }).collect(Collectors.toList());
    }

    public Long getActiveTimeForDate(LocalDate date) {
        List<UsrActivity> activities = usrActivityRepository.findAll()
                .stream()
                .filter(activity -> activity.getDate().toLocalDate().isEqual(date))
                .collect(Collectors.toList());

        long activeTime = activities.stream()
                .mapToLong(activity -> activity.isAfk() ? 0 : 1)  // Add 1 if not AFK
                .sum();

        return activeTime;
    }

    public void resetDailyData() {
        LocalDate today = LocalDate.now();
        List<UsrActivity> activities = usrActivityRepository.findAll()
                .stream()
                .filter(activity -> activity.getDate().toLocalDate().isEqual(today))
                .collect(Collectors.toList());

        activities.forEach(activity -> {
            activity.setAfk(true);
            usrActivityRepository.save(activity);
        });
    }
}




