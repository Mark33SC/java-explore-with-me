package ru.practicum.service.event.dto;

import ru.practicum.service.category.dto.CategoryForEventFullDto;
import ru.practicum.service.event.Event;
import ru.practicum.service.event.Location;
import ru.practicum.service.user.dto.UserForEventFullDto;

public class EventMapper {
    public static Event toEvent(EventCreateDto createDto) {
        Location location = new Location(createDto.getLocation().getLat(), createDto.getLocation().getLon());
        return Event.builder()
                .annotation(createDto.getAnnotation())
                .description(createDto.getDescription())
                .eventDate(createDto.getEventDate())
                .paid(createDto.getPaid())
                .participantLimit(createDto.getParticipantLimit())
                .requestModeration(createDto.getRequestModeration())
                .title(createDto.getTitle())
                .location(location)
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {

        EventShortDto.Category category = EventShortDto.Category.builder()
                .id(event.getCategory().getId())
                .name(event.getCategory().getName())
                .build();

        EventShortDto.User initiator = EventShortDto.User.builder()
                .id(event.getInitiator().getId())
                .name(event.getInitiator().getName())
                .build();

        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(category)
                .eventDate(event.getEventDate())
                .initiator(initiator)
                .paid(event.isPaid())
                .title(event.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        CategoryForEventFullDto category = new CategoryForEventFullDto(
                event.getCategory().getId(),
                event.getCategory().getName()
        );

        UserForEventFullDto initiator = new UserForEventFullDto(
                event.getInitiator().getId(),
                event.getInitiator().getName()
        );

        EventFullDto.Location location = new EventFullDto.Location(
                event.getLocation().getLat(),
                event.getLocation().getLon()
        );

        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(category)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(initiator)
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .location(location)
                .build();
    }
}
