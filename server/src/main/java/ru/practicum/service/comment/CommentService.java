package ru.practicum.service.comment;

import ru.practicum.service.comment.dto.CommentCreateDto;
import ru.practicum.service.comment.dto.CommentFullDto;
import ru.practicum.service.comment.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {

    CommentFullDto getCommentById (long commentId);

    List<Comment> getCommentsByEvent(long eventId, int from, int size);

    void deleteComment(long commentId);
    CommentFullDto addCommentByCurrentUser(Long userId, CommentCreateDto commentCreateDto);

    CommentFullDto updateCommentByCurrentUser(Long userId, CommentUpdateDto commentCreateDto);

    void deleteCommentByCurrentUser(Long userId, Long commentId);
}
