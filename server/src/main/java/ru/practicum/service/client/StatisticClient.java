package ru.practicum.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.service.client.dto.HitDto;
import ru.practicum.service.client.dto.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StatisticClient {

    private final RestTemplate rest;

    private final String serverUrl;

    @Autowired
    public StatisticClient(@Value("${statistic-server.url}") String serverUrl) {
        this.rest = new RestTemplate();
        this.serverUrl = serverUrl;
    }

    public void sendHitAtStaticServer(String app, String uri, String ip) {
        HitDto hit = HitDto.builder().app(app).uri(uri).ip(ip).build();
        HttpEntity<HitDto> request = new HttpEntity<>(hit);
        rest.postForObject(serverUrl + "/hit", request, HitDto.class);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique) {
        String requestUri = serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("start", start.format(formatter));
        urlParam.put("end", end.format(formatter));
        urlParam.put("uris", String.join(",", uris));
        urlParam.put("unique", Boolean.toString(unique));

        ResponseEntity<ViewStats[]> entity = rest.getForEntity(requestUri, ViewStats[].class, urlParam);

        return entity.getBody() != null ? Arrays.asList(entity.getBody()) : Collections.emptyList();
    }
}
