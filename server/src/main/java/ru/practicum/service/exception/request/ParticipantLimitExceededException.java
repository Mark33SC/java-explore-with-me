package ru.practicum.service.exception.request;

import ru.practicum.service.exception.TermNotMetException;

public class ParticipantLimitExceededException extends TermNotMetException {
    public ParticipantLimitExceededException(String reason) {
        super("Participant limit exceeded", reason);
    }
}
