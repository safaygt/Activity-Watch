package com.smartict.activitywatch.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsrActivityDTO {

    private Integer fkUserId;

    private Integer fkWindowActivityId;

    private Integer fkApplicationActivityId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private boolean isAfk;
}
