package ru.practicum.service.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {

    private String message;

    private String reason;

    private HttpStatus status;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

}
