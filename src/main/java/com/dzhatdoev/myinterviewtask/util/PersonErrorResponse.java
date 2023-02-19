package com.dzhatdoev.myinterviewtask.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PersonErrorResponse {
    private String message;

    private LocalDateTime localDateTime;
}