package ru.practicum.service.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.compilation.CompilationService;
import ru.practicum.service.compilation.dto.CompilationDto;
import ru.practicum.service.compilation.dto.CompilationMapper;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationController {

    private final CompilationService compilationService;

    private final CompilationMapper compilationMapper;

    @GetMapping
    public List<CompilationDto> getAllCompilations(
            @RequestParam(required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "100") int size
    ) {
        return compilationService.getAllCompilation(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable long compId) {
        return compilationMapper.toCompilationDto(compilationService.getCompilationById(compId));
    }
}
