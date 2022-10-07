package ru.practicum.service.exception.event;

import ru.practicum.service.exception.AlreadyExistException;

public class EventAlreadyExistException extends AlreadyExistException {
    public EventAlreadyExistException(String reason) {
        super("Event already exist", reason);
    }
}