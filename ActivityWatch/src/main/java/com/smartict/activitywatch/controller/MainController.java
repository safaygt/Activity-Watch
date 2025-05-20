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

    @PostMapping("/reset-day")
    public ResponseEntity<Void> resetDay() {
        try {
            mainService.resetDailyData();
            return ResponseEntity.ok().build();
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

}
