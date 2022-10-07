package ru.practicum.service.exception.request;

import ru.practicum.service.exception.ConflictException;

public class RequesterIsInitiatorEventException extends ConflictException {
    public RequesterIsInitiatorEventException(String reason) {
        super("Requester is initiator for event", reason);
    }
}
