package ru.practicum.service.event.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.event.EventService;
import ru.practicum.service.event.dto.EventFullDto;
import ru.practicum.service.event.dto.EventUpdateDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
public class EventControllerForAdmin {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> searchEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "100") Integer size
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);

        return eventService.searchEvents(users, states, categories, start, end, from, size);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable long eventId) {
        return eventService.publishEvent(eventId);
    }


    @PutMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId, @RequestBody EventUpdateDto updateDto) {
        return eventService.updateEventById(eventId, updateDto);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable long eventId) {
        return eventService.rejectEvent(eventId);
    }
}
