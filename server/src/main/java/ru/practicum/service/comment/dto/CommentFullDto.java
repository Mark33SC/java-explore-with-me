package ru.practicum.service.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentFullDto {

    private Long id;

    private Long user;

    private Long event;

    private LocalDateTime createdOn;

    private String text;

}
