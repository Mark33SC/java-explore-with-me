package ru.practicum.service.exception.comments;

import ru.practicum.service.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(String reason) {
        super("Comment not found", reason);
    }
}
