package ru.practicum.service.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.comment.dto.CommentCreateDto;
import ru.practicum.service.comment.dto.CommentFullDto;
import ru.practicum.service.comment.dto.CommentUpdateDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class CommentControllerForUser {

    private final CommentService commentService;

    @PostMapping("/{userId}/comments")
    public CommentFullDto addComment(
            @PathVariable long userId,
            @RequestBody @Valid CommentCreateDto createDto
    ) {
        return commentService.addCommentByCurrentUser(userId, createDto);
    }

    @PutMapping("/{userId}/comments")
    public CommentFullDto updateComment(
            @PathVariable long userId,
            @RequestBody @Valid CommentUpdateDto updateDto
    ) {
        return commentService.updateCommentByCurrentUser(userId, updateDto);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public void deleteByUser(@PathVariable long userId, @PathVariable long commentId) {
        commentService.deleteCommentByCurrentUser(userId, commentId);
    }
}