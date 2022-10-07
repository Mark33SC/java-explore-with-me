package ru.practicum.service.exception.event;

import ru.practicum.service.exception.ConflictException;

public class UserIsNotInitiatorException extends ConflictException {
    public UserIsNotInitiatorException(String reason) {
        super("User is not initiator", reason);
    }
}
