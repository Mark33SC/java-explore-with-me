package ru.practicum.service.exception.request;

import ru.practicum.service.exception.NotFoundException;

public class RequestNotFoundException extends NotFoundException {
    public RequestNotFoundException(String reason) {
        super("Request not found", reason);
    }
}
