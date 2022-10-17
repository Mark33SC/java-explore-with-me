package ru.practicum.statistics.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ViewStats {

    private String app;

    private String uri;

    private Integer hits;

}
