package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.compilation.dto.CompilationCreateDto;
import ru.practicum.service.compilation.dto.CompilationDto;
import ru.practicum.service.compilation.dto.CompilationMapper;
import ru.practicum.service.event.Event;
import ru.practicum.service.event.EventRepository;
import ru.practicum.service.exception.compilation.CompilationNotFoundException;
import ru.practicum.service.exception.event.EventAlreadyExistException;
import ru.practicum.service.exception.event.EventNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    private final CompilationMapper mapper;

    @Override
    public CompilationDto addCompilation(CompilationCreateDto createDto) {
        Compilation compilation = mapper.toCompilation(createDto);
        List<Event> events = findAllEventById(createDto.getEvents());

        compilation.setEvents(events);
        compilationRepository.save(compilation);

        return mapper.toCompilationDto(compilation);
    }

    @Override
    public void deleteCompilationById(long id) {
        compilationRepository.deleteById(id);
    }

    @Override
    public void deleteEventFromCompilation(long compilationId, long eventId) {
        Compilation compilation = getCompilationById(compilationId);
        Event event = findEventById(eventId);

        List<Event> events = compilation.getEvents();
        if (events.contains(event)) {
            events.remove(event);
        } else {
            throw new EventNotFoundException(
                    String.format(
                            "Event with id:%s not found to event list from compilation with id:%s",
                            eventId,
                            compilationId
                    )
            );
        }
    }

    @Override
    public void addEventToCompilation(long compilationId, long eventId) {
        Compilation compilation = getCompilationById(compilationId);
        Event event = findEventById(eventId);

        List<Event> events = compilation.getEvents();
        if (events.contains(event)) {
            throw new EventAlreadyExistException(
                    String.format("Event with id:%s already exist to compilation with id:%s", eventId, compilationId)
            );
        } else {
            events.add(event);
        }
    }

    @Override
    public void unpinnedCompilationById(long id) {
        Compilation compilation = getCompilationById(id);
        compilation.setPinned(false);
    }

    @Override
    public void pinnedCompilationById(long id) {
        Compilation compilation = getCompilationById(id);
        compilation.setPinned(true);
    }

    @Override
    public List<CompilationDto> getAllCompilation(Boolean pinned, int from, int size) {
        List<Compilation> comp = compilationRepository.findAll();

        return comp.stream().map(mapper::toCompilationDto).collect(Collectors.toList());
    }

    @Override
    public Compilation getCompilationById(long id) {
        return compilationRepository.findById(id).orElseThrow(
                () -> new CompilationNotFoundException(String.format("Compilation with id:%s not found", id))
        );
    }

    private Event findEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new EventNotFoundException(String.format("Event with id:%s not found", id))
        );
    }

    private List<Event> findAllEventById(List<Long> ids) {
        return eventRepository.findAllById(ids);
    }
}
