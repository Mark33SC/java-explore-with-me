package ru.practicum.statistics.endpoint;

import ru.practicum.statistics.endpoint.dto.EndpointHitCreateDto;
import ru.practicum.statistics.endpoint.dto.EndpointHitDto;
import ru.practicum.statistics.endpoint.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitService {

    EndpointHitDto saveHit(EndpointHitCreateDto createDto);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

}