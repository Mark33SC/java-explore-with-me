package ru.practicum.statistics.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistics.endpoint.dto.EndpointHitCreateDto;
import ru.practicum.statistics.endpoint.dto.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EndpointHitController {

    private final EndpointHitService service;

    @PostMapping("/hit")
    public void saveVisiting(@RequestBody EndpointHitCreateDto createDto) {
        service.saveHit(createDto);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime rangeStart = LocalDateTime.parse(start, formatter);
        LocalDateTime rangeEnd = LocalDateTime.parse(end, formatter);

        return service.getStats(rangeStart, rangeEnd, uris, unique);
    }
}