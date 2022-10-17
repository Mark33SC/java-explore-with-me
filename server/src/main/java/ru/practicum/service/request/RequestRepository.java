package ru.practicum.service.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.event.Event;
import ru.practicum.service.user.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequester(User user);

    List<Request> findAllByEvent(Event event);

    List<Request> findByEvent(Event event);

}