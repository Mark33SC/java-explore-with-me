package ru.practicum.service.compilation.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.service.category.dto.CategoryForCompilationDto;
import ru.practicum.service.compilation.Compilation;
import ru.practicum.service.event.dto.EventForCompilationDto;
import ru.practicum.service.request.RequestRepository;
import ru.practicum.service.user.dto.UserForCompilationDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final RequestRepository requestRepository;

    public Compilation toCompilation(CompilationCreateDto createDto) {
        return Compilation.builder()
                .pinned(createDto.getPinned())
                .title(createDto.getTitle())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        List<EventForCompilationDto> events = compilation.getEvents().stream()
                .map(e -> {
                    CategoryForCompilationDto category = new CategoryForCompilationDto(
                            e.getCategory().getId(),
                            e.getCategory().getName()
                    );

                    int confirmedRequest = requestRepository.findByEvent(e).size();

                    UserForCompilationDto initiator = new UserForCompilationDto(
                            e.getInitiator().getId(),
                            e.getInitiator().getName()
                    );

                    return EventForCompilationDto.builder()
                            .id(e.getId())
                            .annotation(e.getAnnotation())
                            .category(category)
                            .confirmedRequests(confirmedRequest)
                            .eventDate(e.getEventDate())
                            .initiator(initiator)
                            .paid(e.isPaid())
                            .title(e.getTitle())
                            .build();
                }).collect(Collectors.toList());


        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(events)
                .build();
    }
}
