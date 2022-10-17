package ru.practicum.statistics.endpoint.requestparams;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GetStatsParams {

    @Builder.Default
    private LocalDateTime start = LocalDateTime.MIN;

    @Builder.Default
    private LocalDateTime end = LocalDateTime.MAX;

    private List<String> uris;

    private boolean unique;

}
