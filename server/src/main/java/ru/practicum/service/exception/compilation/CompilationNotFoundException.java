package ru.practicum.service.exception.compilation;

import ru.practicum.service.exception.NotFoundException;

public class CompilationNotFoundException extends NotFoundException {
    public CompilationNotFoundException(String reason) {
        super("Compilation not found", reason);
    }
}