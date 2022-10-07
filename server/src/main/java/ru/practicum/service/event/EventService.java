package ru.practicum.service.event;

import ru.practicum.service.event.dto.EventCreateDto;
import ru.practicum.service.event.dto.EventFullDto;
import ru.practicum.service.event.dto.EventShortDto;
import ru.practicum.service.event.dto.EventUpdateDto;
import ru.practicum.service.request.dto.RequestFullDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> searchEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventSort sort,
            int from,
            int size
    );

    EventFullDto getEventById(long id);

    List<EventShortDto> getEventsByInitiatorId(long userId, int from, int size);

    EventFullDto updateEventByInitiatorId(long userId, EventUpdateDto eventUpdateDto);

    EventFullDto updateEventById(long eventId, EventUpdateDto eventUpdateDto);

    EventFullDto addEvent(long userId, EventCreateDto eventCreateDto);

    EventFullDto getEventCurrentUserById(long userId, long eventId);

    EventFullDto cancelEventAddedCurrentUserById(long userId, long eventId);

    List<RequestFullDto> getRequestsForEventCurrentUserById(long userId, long eventId);

    RequestFullDto confirmRequestOnEventCurrentUser(long userId, long eventId, long reqId);

    RequestFullDto rejectRequestOnEventCurrentUser(long userId, long eventId, long reqId);

    EventFullDto publishEvent(long eventId);

    EventFullDto rejectEvent(long eventId);

    List<EventFullDto> searchEvents(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    );
}
