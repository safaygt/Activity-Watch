package com.smartict.activitywatch.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsrActivityResponseDTO {
    private Integer id;
    private String username;
    private String activityName;
    private LocalDateTime date;
    private boolean isAfk;
}
