package ru.practicum.service.exception.event;

import ru.practicum.service.exception.TermNotMetException;

public class EventDateInvalidException extends TermNotMetException {
    public EventDateInvalidException(String reason) {
        super("Event date invalid", reason);
    }
}