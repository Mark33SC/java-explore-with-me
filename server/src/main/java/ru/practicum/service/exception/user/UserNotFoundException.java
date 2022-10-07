package ru.practicum.service.exception.user;

import ru.practicum.service.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String reason) {
        super("User not found", reason);
    }

}
