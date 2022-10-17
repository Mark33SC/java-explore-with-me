package ru.practicum.service.compilation;

import ru.practicum.service.compilation.dto.CompilationCreateDto;
import ru.practicum.service.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto addCompilation(CompilationCreateDto createDto);

    void deleteCompilationById(long id);

    void deleteEventFromCompilation(long compilationId, long eventId);

    void addEventToCompilation(long compilationIdm, long eventId);

    void unpinnedCompilationById(long id);

    void pinnedCompilationById(long id);

    List<CompilationDto> getAllCompilation(Boolean pinned, int from, int size);

    Compilation getCompilationById(long id);

}
