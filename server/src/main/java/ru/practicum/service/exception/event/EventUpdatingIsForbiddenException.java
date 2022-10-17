package ru.practicum.service.exception.event;

import ru.practicum.service.exception.TermNotMetException;

public class EventUpdatingIsForbiddenException extends TermNotMetException {
    public EventUpdatingIsForbiddenException(String reason) {
        super("Event updating is forbidden", reason);
    }
}