package ru.practicum.service.event.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class EventShortDto {
    @EqualsAndHashCode.Include
    private long id;

    private String annotation;

    private Category category;

    private LocalDateTime eventDate;

    private User initiator;

    private boolean paid;

    private String title;

    private long confirmedRequests;

    private int views;

    @Data
    @Builder
    public static class Category {

        private long id;

        private String name;

    }

    @Data
    @Builder
    public static class User {

        private long id;

        private String name;

    }

}
