package ru.practicum.service.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.category.dto.CategoryForCompilationDto;
import ru.practicum.service.user.dto.UserForCompilationDto;

import java.time.LocalDateTime;

@Data
@Builder
public class EventForCompilationDto {

    private Long id;

    private String annotation;

    private CategoryForCompilationDto category;

    private Integer confirmedRequests;

    private LocalDateTime eventDate;

    private UserForCompilationDto initiator;

    private Boolean paid;

    private String title;

    private Integer views;

}