package ru.practicum.service.comment.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.service.comment.Comment;
import ru.practicum.service.event.Event;
import ru.practicum.service.event.EventRepository;
import ru.practicum.service.exception.event.EventNotFoundException;
import ru.practicum.service.user.User;
import ru.practicum.service.user.UserService;

@Component
@AllArgsConstructor
public class CommentMapper {
    private EventRepository eventRepository;
    private UserService userService;

    public Comment toComment(CommentCreateDto commentCreateDto) {
        Event event =  findEventById(commentCreateDto.getEvent());

        return Comment.builder()
                .event(event)
                .text(commentCreateDto.getText())
                .build();
    }

    public Comment toComment(CommentFullDto commentFullDto) {
        Event event = findEventById(commentFullDto.getEvent());
        User user = userService.getUserById(commentFullDto.getUser());
        return Comment.builder()
                .id(commentFullDto.getId())
                .text(commentFullDto.getText())
                .user(user)
                .createdOn(commentFullDto.getCreatedOn())
                .event(event)
                .build();
    }

    public CommentFullDto toFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .user(comment.getUser().getId())
                .event(comment.getEvent().getId())
                .createdOn(comment.getCreatedOn())
                .text(comment.getText())
                .build();
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EventNotFoundException(String.format("Event with id:$s not found", eventId))
        );
    }
}
