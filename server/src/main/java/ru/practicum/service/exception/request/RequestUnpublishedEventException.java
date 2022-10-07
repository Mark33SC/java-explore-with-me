package ru.practicum.service.exception.request;

import ru.practicum.service.exception.TermNotMetException;

public class RequestUnpublishedEventException extends TermNotMetException {
    public RequestUnpublishedEventException(String reason) {
        super("Request unpublished event forbidden", reason);
    }
}
