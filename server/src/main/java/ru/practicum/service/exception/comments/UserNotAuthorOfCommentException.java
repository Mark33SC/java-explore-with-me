package ru.practicum.service.exception.comments;

import ru.practicum.service.exception.TermNotMetException;

public class UserNotAuthorOfCommentException extends TermNotMetException {
    public UserNotAuthorOfCommentException(String reason) {
        super("User not author of comment", reason);
    }
}
