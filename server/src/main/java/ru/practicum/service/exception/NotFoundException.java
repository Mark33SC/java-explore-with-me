package ru.practicum.service.exception;

public class NotFoundException extends RuntimeException {

    private final String reason;

    public NotFoundException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

}
