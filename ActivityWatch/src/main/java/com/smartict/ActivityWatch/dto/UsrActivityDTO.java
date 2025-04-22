package com.smartict.ActivityWatch.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsrActivityDTO {

    private Integer fkuserId;

    private Integer fkactivityId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private boolean isAfk;
}
