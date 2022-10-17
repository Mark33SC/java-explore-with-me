package ru.practicum.statistics.endpoint.dto;

import ru.practicum.statistics.endpoint.EndpointHit;

public class EndpointHitMapper {
    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

    public static EndpointHit toEndpointHit(EndpointHitCreateDto createDto) {
        return EndpointHit.builder()
                .app(createDto.getApp())
                .uri(createDto.getUri())
                .ip(createDto.getIp())
                .build();
    }
}
