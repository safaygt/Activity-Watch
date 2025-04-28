package com.smartict.activitywatch.controller;

import com.smartict.activitywatch.dto.UsrActivityDTO;
import com.smartict.activitywatch.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/report")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @PostMapping("/log")
    public ResponseEntity<String> logActivity(@RequestBody UsrActivityDTO dto) {
        try {
            mainService.logActivity(dto);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Activity logged successfully.");
    }
}
