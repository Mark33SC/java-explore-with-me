package ru.practicum.statistics.endpoint;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
}
