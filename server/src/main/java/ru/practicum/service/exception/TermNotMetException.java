package ru.practicum.service.exception;

public class TermNotMetException extends RuntimeException {

    private final String reason;

    public TermNotMetException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

}