package ru.practicum.service.exception.comments;

import ru.practicum.service.exception.TermNotMetException;

public class CommentNotValidUpdateDate extends TermNotMetException {
    public CommentNotValidUpdateDate(String reason) {
        super("Comment updating is forbidden", reason);
    }
}
