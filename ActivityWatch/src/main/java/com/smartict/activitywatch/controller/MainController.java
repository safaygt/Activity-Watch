package com.smartict.activitywatch.controller;

import com.smartict.activitywatch.dto.UsrActivityResponseDTO;
import com.smartict.activitywatch.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("v1/report")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping("/active-time/{date}")
    public ResponseEntity<Long> getActiveTime(@PathVariable String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            Long activeTime = mainService.getActiveTimeForDate(parsedDate);
            return ResponseEntity.ok(activeTime);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



    @GetMapping("/log")
    public ResponseEntity<List<UsrActivityResponseDTO>> getLog(@RequestParam("date") String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            List<UsrActivityResponseDTO> activities = mainService.getAllActivities(parsedDate);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/log/monthly")
    public ResponseEntity<List<UsrActivityResponseDTO>> getMonthlyLog(@RequestParam("year") int year, @RequestParam("month") int month) {
        try {
            List<UsrActivityResponseDTO> activities = mainService.getMonthlyActivities(year, month);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/active-time/monthly")
    public ResponseEntity<Long> getMonthlyActiveTime(
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        try {
            Long activeTime = mainService.getActiveTimeForMonth(year, month);
            return ResponseEntity.ok(activeTime);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



}

