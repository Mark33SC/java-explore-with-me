package ru.practicum.service.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.event.dto.EventForCompilationDto;

import java.util.List;

@Data
@Builder
public class CompilationDto {

    private Long id;

    private String title;

    private boolean pinned;

    private List<EventForCompilationDto> events;
}