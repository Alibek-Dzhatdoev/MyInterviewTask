package com.dzhatdoev.myinterviewtask.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class PersonErrorResponse {
    private String message;

    private LocalDateTime localDateTime;
}