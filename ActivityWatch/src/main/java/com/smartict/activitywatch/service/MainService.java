package com.smartict.activitywatch.service;

import com.smartict.activitywatch.dto.UsrActivityResponseDTO;
import com.smartict.activitywatch.entity.UsrActivity;
import com.smartict.activitywatch.repository.UsrActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MainService {

    private final UsrActivityRepository usrActivityRepository;

    public List<UsrActivityResponseDTO> getAllActivities() {
        LocalDate today = LocalDate.now();
        List<UsrActivity> activities = usrActivityRepository.findAll()
                .stream()
                .filter(activity -> activity.getDate().toLocalDate().isEqual(today)) // Sadece bugünün verilerini al
                .sorted((a, b) -> b.getDate().compareTo(a.getDate())) // Tarihe göre DESC sırala
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
}

