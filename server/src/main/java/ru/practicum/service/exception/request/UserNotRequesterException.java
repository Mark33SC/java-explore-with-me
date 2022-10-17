package ru.practicum.service.exception.request;

import ru.practicum.service.exception.ConflictException;

public class UserNotRequesterException extends ConflictException {
    public UserNotRequesterException(String reason) {
        super("User not requester for request", reason);
    }
}
