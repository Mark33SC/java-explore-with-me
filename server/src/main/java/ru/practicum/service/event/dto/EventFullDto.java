package ru.practicum.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.service.category.dto.CategoryForEventFullDto;
import ru.practicum.service.event.EventState;
import ru.practicum.service.user.dto.UserForEventFullDto;

import java.time.LocalDateTime;

@Data
@Builder
public class EventFullDto {

    private long id;

    private String annotation;

    private CategoryForEventFullDto category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserForEventFullDto initiator;

    private boolean paid;

    private long participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private EventState state;

    private String title;

    private long confirmedRequests;

    private int views;

    private Location location;

    @Data
    @AllArgsConstructor
    public static class Location {

        private double lat;

        private double lon;

    }
}