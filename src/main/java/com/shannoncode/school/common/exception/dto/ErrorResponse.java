package com.shannoncode.school.common.exception.dto;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timeStamp, int status, String error, String message) {
}