package ru.practicum.service.exception.user;

import ru.practicum.service.exception.TermNotMetException;

public class UserNotActivatedException extends TermNotMetException {
    public UserNotActivatedException(String reason) {
        super("User not activated", reason);
    }
}