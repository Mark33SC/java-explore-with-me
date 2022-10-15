package ru.practicum.service.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CommentUpdateDto {

    @NotNull
    private Long id;

    @NotBlank
    private String text;

}

