package ru.practicum.service.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.compilation.CompilationService;
import ru.practicum.service.compilation.dto.CompilationCreateDto;
import ru.practicum.service.compilation.dto.CompilationDto;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationControllerForAdmin {

    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto addCompilation(@RequestBody CompilationCreateDto createDto) {
        return compilationService.addCompilation(createDto);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable long compId, @PathVariable long eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinnedCompilationById(@PathVariable long compId) {
        compilationService.pinnedCompilationById(compId);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable long compId) {
        compilationService.deleteCompilationById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinnedCompilationById(@PathVariable long compId) {
        compilationService.unpinnedCompilationById(compId);
    }
}
