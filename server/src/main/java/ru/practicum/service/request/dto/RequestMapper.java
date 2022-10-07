package ru.practicum.service.request.dto;

import ru.practicum.service.request.Request;

public class RequestMapper {
    public static RequestFullDto toRequestFullDto(Request request) {
        return RequestFullDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }
}