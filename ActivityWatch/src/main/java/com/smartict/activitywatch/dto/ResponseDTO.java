package com.smartict.activitywatch.dto;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseDTO {

private HttpStatus responseCode;
private Integer userId;
private String username;
}
