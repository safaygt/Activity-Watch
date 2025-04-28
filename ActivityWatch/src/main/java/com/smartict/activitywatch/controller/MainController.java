package com.smartict.activitywatch.controller;

import com.smartict.activitywatch.dto.UsrActivityDTO;
import com.smartict.activitywatch.dto.UsrActivityResponseDTO;
import com.smartict.activitywatch.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/report")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;



    @GetMapping("/log")
    public ResponseEntity<List<UsrActivityResponseDTO>> getLog() {
        try {
            List<UsrActivityResponseDTO> activities = mainService.getAllActivities();
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }





}
