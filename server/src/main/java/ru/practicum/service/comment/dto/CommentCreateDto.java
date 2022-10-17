package ru.practicum.service.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class CommentCreateDto {

    @NotNull
    private Long event;

    @NotBlank
    @Size(max = 5000)
    private String text;
}
