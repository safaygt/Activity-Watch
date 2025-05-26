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
                .filter(activity -> activity.getDate().toLocalDate().isEqual(date)) // Aynı günü alıyoruz
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

    public List<UsrActivityResponseDTO> getMonthlyActivities(int year, int month) {
        // Veritabanında sadece yıl ve ay'ı eşleştirerek filtreleme yapıyoruz
        List<UsrActivity> activities = usrActivityRepository.findAll()
                .stream()
                .filter(activity -> activity.getDate().getYear() == year && activity.getDate().getMonthValue() == month)
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))  // Son tarihler önce olacak şekilde sıralama
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
                .mapToLong(activity -> activity.isAfk() ? 0 : 1)
                .sum();

        return activeTime;
    }
    public Long getActiveTimeForMonth(int year, int month) {
        return usrActivityRepository.findAll().stream()
                .filter(a -> {
                    LocalDate d = a.getDate().toLocalDate();
                    return d.getYear() == year && d.getMonthValue() == month;
                })
                .filter(a -> !a.isAfk())
                .count(); // her kayıt 1dk
    }

}

