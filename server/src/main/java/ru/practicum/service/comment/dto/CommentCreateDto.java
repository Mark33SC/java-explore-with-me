package ru.practicum.service.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CommentCreateDto {

    @NotNull
    private Long event;

    @NotBlank
    private String text;
}
