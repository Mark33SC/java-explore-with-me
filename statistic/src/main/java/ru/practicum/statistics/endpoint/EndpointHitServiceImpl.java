package ru.practicum.statistics.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statistics.endpoint.dto.EndpointHitCreateDto;
import ru.practicum.statistics.endpoint.dto.EndpointHitDto;
import ru.practicum.statistics.endpoint.dto.EndpointHitMapper;
import ru.practicum.statistics.endpoint.dto.ViewStats;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {

    private final EndpointHitRepository repository;

    private final EntityManager entityManager;

    @Override
    public EndpointHitDto saveHit(EndpointHitCreateDto createDto) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(createDto);
        LocalDateTime now = LocalDateTime.now();
        endpointHit.setTimestamp(now);

        return EndpointHitMapper.toEndpointHitDto(repository.save(endpointHit));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<EndpointHit> root = query.from(EndpointHit.class);
        Predicate[] predicates = new Predicate[2];

        predicates[0] = root.get(EndpointHit_.uri).in(uris);
        predicates[1] = builder.between(root.get(EndpointHit_.timestamp), start, end);

        query.where(predicates);

        if (unique) {
            query.multiselect(root.get(EndpointHit_.app), root.get(EndpointHit_.uri), builder.countDistinct(root.get(EndpointHit_.ip)));
        } else {
            query.multiselect(root.get(EndpointHit_.app), root.get(EndpointHit_.uri), builder.count(root));
        }

        query.groupBy(root.get(EndpointHit_.app), root.get(EndpointHit_.uri));

        return entityManager.createQuery(query).getResultStream().map(item -> ViewStats
                .builder()
                .app(item[0].toString())
                .uri(item[1].toString())
                .hits(Integer.valueOf(item[2].toString()))
                .build()).collect(Collectors.toList());
    }
}