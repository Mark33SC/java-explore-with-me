package ru.practicum.service.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.client.StatisticClient;
import ru.practicum.service.event.EventService;
import ru.practicum.service.event.dto.EventFullDto;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;
    private final StatisticClient statisticClient;

//    @GetMapping
//    public List<EventShortDto> getEventsWithFiltering(
//            @RequestParam String text,
//            @RequestParam(required = false) List<Long> categories,
//            @RequestParam(required = false) Boolean paid,
//            @RequestParam(required = false) String rangeStart,
//            @RequestParam(required = false) String rangeEnd,
//            @RequestParam(required = false) Boolean onlyAvailable,
//            @RequestParam(required = false) String sort,
//            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
//            @Positive @RequestParam(defaultValue = "100") int size,
//            HttpServletRequest request
//    ) {
//        LocalDateTime start = null;
//        LocalDateTime end = null;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        if (rangeStart != null) {
//            start = LocalDateTime.parse(rangeStart, formatter);
//        }
//
//        if (rangeEnd != null) {
//            end = LocalDateTime.parse(rangeEnd, formatter);
//        }
//
//        EventSort eventSort = EventSort.valueOf(sort);
//
//        statisticClient.sendHitAtStaticServer("ExploreWithMe", request.getRequestURI(), request.getRemoteAddr());
//
//        return eventService.getEvents(text, categories, paid, start, end, onlyAvailable, eventSort, from, size);
//    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable long eventId, HttpServletRequest request) {
        statisticClient.sendHitAtStaticServer("ExploreWithMe", request.getRequestURI(), request.getRemoteAddr());

        return eventService.getEventById(eventId);
    }
}
