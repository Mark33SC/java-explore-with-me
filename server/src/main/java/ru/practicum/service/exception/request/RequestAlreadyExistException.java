package ru.practicum.service.exception.request;

import ru.practicum.service.exception.AlreadyExistException;

public class RequestAlreadyExistException extends AlreadyExistException {
    public RequestAlreadyExistException(String reason) {
        super("Request already exist", reason);
    }
}
