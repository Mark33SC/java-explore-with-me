package ru.practicum.service.exception.event;

import ru.practicum.service.exception.NotFoundException;

public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(String reason) {
        super("Event not found", reason);
    }
}