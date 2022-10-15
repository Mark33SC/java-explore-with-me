package ru.practicum.service.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.comment.Comment;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.comment.dto.CommentFullDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public CommentFullDto getById(@PathVariable long commentId) {
        return commentService.getCommentById(commentId);
    }

    @GetMapping("/{eventId}")
    public List<Comment> getByEvent(
            @PathVariable long eventId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size)
    {
        return commentService.getCommentsByEvent(eventId, from, size);
    }
}
