package ru.practicum.service.event.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EventUpdateDto {

    private long eventId;

    private String annotation;

    private Long category;

    private String description;

    private LocalDateTime eventDate;

    private Boolean paid;

    private Long participantLimit;

    private String title;

}
