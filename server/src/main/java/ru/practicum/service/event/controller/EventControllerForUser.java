package ru.practicum.service.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.event.EventService;
import ru.practicum.service.event.dto.EventCreateDto;
import ru.practicum.service.event.dto.EventFullDto;
import ru.practicum.service.event.dto.EventShortDto;
import ru.practicum.service.event.dto.EventUpdateDto;
import ru.practicum.service.request.dto.RequestFullDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "users")
@RequiredArgsConstructor
@Validated
public class EventControllerForUser {

    private final EventService eventService;

    @PostMapping("/{userId}/events")
    public EventFullDto addEvent(@PathVariable long userId, @RequestBody @Valid EventCreateDto createDto) {
        return eventService.addEvent(userId, createDto);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto updateEvent(@PathVariable long userId, @RequestBody @Valid EventUpdateDto updateDto) {
        return eventService.updateEventByInitiatorId(userId, updateDto);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getAllEventByInitiator(
            @PathVariable long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        return eventService.getEventsByInitiatorId(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getFullInfoAboutEventById(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.getEventCurrentUserById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEventAddedCurrentUserById(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.cancelEventAddedCurrentUserById(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestFullDto> getRequestsToParticipateInEventCurrentUser(
            @PathVariable long userId,
            @PathVariable long eventId
    ) {
        return eventService.getRequestsForEventCurrentUserById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestFullDto confirmRequestForEventCurrentUser(
            @PathVariable long userId,
            @PathVariable long eventId,
            @PathVariable long reqId
    ) {
        return eventService.confirmRequestOnEventCurrentUser(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestFullDto rejectRequestForEventCurrentUser(
            @PathVariable long userId,
            @PathVariable long eventId,
            @PathVariable long reqId
    ) {
        return eventService.rejectRequestOnEventCurrentUser(userId, eventId, reqId);
    }

}
