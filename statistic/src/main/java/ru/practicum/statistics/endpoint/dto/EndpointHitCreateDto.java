package ru.practicum.statistics.endpoint.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EndpointHitCreateDto {

    private String app;

    private String uri;

    private String ip;

}
