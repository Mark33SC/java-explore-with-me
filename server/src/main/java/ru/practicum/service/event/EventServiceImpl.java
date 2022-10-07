package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.service.category.Category;
import ru.practicum.service.category.CategoryRepository;
import ru.practicum.service.client.StatisticClient;
import ru.practicum.service.client.dto.ViewStats;
import ru.practicum.service.event.dto.*;
import ru.practicum.service.exception.category.CategoryNotFoundException;
import ru.practicum.service.exception.event.EventDateInvalidException;
import ru.practicum.service.exception.event.EventNotFoundException;
import ru.practicum.service.exception.event.EventUpdatingIsForbiddenException;
import ru.practicum.service.exception.event.UserIsNotInitiatorException;
import ru.practicum.service.exception.request.ParticipantLimitExceededException;
import ru.practicum.service.exception.request.RequestNotFoundException;
import ru.practicum.service.exception.user.UserNotActivatedException;
import ru.practicum.service.request.Request;
import ru.practicum.service.request.RequestRepository;
import ru.practicum.service.request.RequestStatus;
import ru.practicum.service.request.dto.RequestFullDto;
import ru.practicum.service.request.dto.RequestMapper;
import ru.practicum.service.user.User;
import ru.practicum.service.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EventServiceImpl implements EventService {

    private static final int HOURS_BEFORE_EVENT = 2;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final StatisticClient statisticClient;

    private static Event getEventWithMinCreatedOn(List<Event> events) {
        return events.stream().min(Comparator.comparing(Event::getCreatedOn)).orElseThrow();
    }

    @Override
    public List<EventShortDto> searchEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventSort sort,
            int from,
            int size
    ) {
        switch (sort) {
            case EVENT_DATE:
                return getEventsSortByEventDate(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size);
            case VIEWS:
                return getEventsSortByViews(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size);
            default:
                return getEventsWithoutSort(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size);
        }
    }

    @Override
    public EventFullDto getEventById(long id) {
        Event event = findEventById(id);
        addViewsAndRequests(event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEventsByInitiatorId(long userId, int from, int size) {
        log.info("Get events for initiator with id:{}, from: {}, size:{}", userId, from, size);

        User initiator = userService.getUserById(userId);

        PageRequest request = PageRequest.of(from, size);

        List<Event> events = eventRepository.findAllByInitiator(initiator, request).toList();

        return addViewsAndRequestsForEventShortDto(events);
    }

    @Override
    public EventFullDto updateEventByInitiatorId(long userId, EventUpdateDto eventUpdateDto) {

        log.info(
                "Update event with id:{} at user with id:{}. Update data: {}",
                eventUpdateDto.getEventId(), userId, eventUpdateDto
        );

        long eventId = eventUpdateDto.getEventId();
        Event event = findEventById(eventId);

        isInitiator(event, userId);

        return updateEventById(eventId, eventUpdateDto);
    }

    @Override
    public EventFullDto updateEventById(long eventId, EventUpdateDto eventUpdateDto) {
        LocalDateTime now = LocalDateTime.now();

        Event event = findEventById(eventId);

        boolean isValidEventState = event.getState() == EventState.PENDING
                || event.getState() == EventState.CANCELED;


        if (!isValidEventState) {
            throw new EventUpdatingIsForbiddenException("Update status is doesn't CANCELED or PENDING");
        }

        if (eventUpdateDto.getCategory() != null) {
            long categoryId = eventUpdateDto.getCategory();

            Category category = categoryRepository.findById(categoryId).orElseThrow(
                    () -> new CategoryNotFoundException(String.format("Category with id:%s not found", categoryId))
            );

            event.setCategory(category);
        }

        if (eventUpdateDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdateDto.getAnnotation());
        }

        if (eventUpdateDto.getDescription() != null) {
            event.setDescription(eventUpdateDto.getDescription());
        }

        if (eventUpdateDto.getEventDate() != null) {
            boolean isValidEventDate = eventUpdateDto.getEventDate().minusHours(HOURS_BEFORE_EVENT).isAfter(now);

            if (!isValidEventDate) {
                throw new EventDateInvalidException(String.format(
                        "It is forbidden to update events date no earlier than %s hours before the event",
                        HOURS_BEFORE_EVENT));
            }

            event.setEventDate(eventUpdateDto.getEventDate());
        }

        if (eventUpdateDto.getPaid() != null) {
            event.setPaid(eventUpdateDto.getPaid());
        }

        if (eventUpdateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }

        if (eventUpdateDto.getTitle() != null) {
            event.setTitle(eventUpdateDto.getTitle());
        }

        addViewsAndRequests(event);

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto addEvent(long userId, EventCreateDto eventCreateDto) {
        LocalDateTime now = LocalDateTime.now();
        boolean isValidEventDate = eventCreateDto.getEventDate().minusHours(HOURS_BEFORE_EVENT).isAfter(now);

        User user = userService.getUserById(userId);

        if (!user.isActivated()) {
            throw new UserNotActivatedException(String.format("User with id:%s is not activated", userId));
        }

        if (!isValidEventDate) {
            throw new EventDateInvalidException(String.format(
                    "It is forbidden to create events no earlier than %s hours before the event",
                    HOURS_BEFORE_EVENT));
        }


        Event event = EventMapper.toEvent(eventCreateDto);

        event.setCategory(categoryRepository.findById(eventCreateDto.getCategory()).orElseThrow(
                () -> new CategoryNotFoundException(
                        String.format("Category with id:%s not found", eventCreateDto.getCategory())
                )
        ));

        event.setCreatedOn(now);

        event.setInitiator(user);

        event.setState(EventState.PENDING);

        eventRepository.save(event);

        addViewsAndRequests(event);

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto getEventCurrentUserById(long userId, long eventId) {
        log.info("Get event with id:{} for current user with id:{}", eventId, userId);
        Event event = findEventById(eventId);

        isInitiator(event, userId);
        addViewsAndRequests(event);

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto cancelEventAddedCurrentUserById(long userId, long eventId) {
        log.info("Cancel event with id:{} added current user with id:{}", eventId, userId);

        Event event = findEventById(eventId);

        isInitiator(event, userId);

        event.setState(EventState.CANCELED);

        long confirmedRequestCount = getConfirmedRequestsCountForEvent(event);
        EventFullDto fullDto = EventMapper.toEventFullDto(event);
        fullDto.setConfirmedRequests(confirmedRequestCount);
        addViewsAndRequests(event);

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<RequestFullDto> getRequestsForEventCurrentUserById(long userId, long eventId) {
        Event event = findEventById(eventId);

        isInitiator(event, userId);

        List<Request> requests = requestRepository.findAllByEvent(event);

        return requests.stream().map(RequestMapper::toRequestFullDto).collect(Collectors.toList());
    }

    @Override
    public RequestFullDto confirmRequestOnEventCurrentUser(long userId, long eventId, long reqId) {
        Event event = findEventById(eventId);
        isInitiator(event, userId);
        Request request = getRequestById(reqId);

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            return RequestMapper.toRequestFullDto(request);
        }

        int requestCount = getRequestsByEvent(event).size();

        if (event.getParticipantLimit() <= requestCount) {
            throw new ParticipantLimitExceededException(String.format(
                    "Exceeding limit of participants for event with id:%s. Request with id:%s cannot be confirmed",
                    eventId, reqId
            ));
        }

        if (event.getParticipantLimit() <= requestCount + 1) {
            rejectAllPendingRequestByEvent(event);
        }

        request.setStatus(RequestStatus.CONFIRMED);

        return RequestMapper.toRequestFullDto(request);
    }

    @Override
    public RequestFullDto rejectRequestOnEventCurrentUser(long userId, long eventId, long reqId) {
        Event event = findEventById(eventId);
        isInitiator(event, userId);
        Request request = getRequestById(reqId);
        request.setStatus(RequestStatus.REJECTED);

        return RequestMapper.toRequestFullDto(request);
    }

    @Override
    public EventFullDto publishEvent(long eventId) {
        log.info("Publish event with id:{}", eventId);
        Event event = findEventById(eventId);
        event.setState(EventState.PUBLISHED);
        addViewsAndRequests(event);

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto rejectEvent(long eventId) {
        log.info("Reject event with id:{}", eventId);
        Event event = findEventById(eventId);
        event.setState(EventState.CANCELED);
        addViewsAndRequests(event);

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventFullDto> searchEvents(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    ) {
        PageRequest request = PageRequest.of(from, size);
        Specification<Event> specEvent = EventSpecs
                .hasInitiationIds(users)
                .and(EventSpecs.hasEventCategory(categories));

        List<Event> events = eventRepository.findAll(specEvent, request)
                .filter(e -> states.contains(e.getState().getVal())).toList();

        return addViewsAndRequestsForEventFullDto(events);
    }

    private Event findEventById(long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new EventNotFoundException(String.format("Event with id:%s not found", id))
        );
    }

    private List<Request> getRequestsByEvent(Event event) {
        return requestRepository.findAllByEvent(event);
    }

    private Request getRequestById(long reqId) {
        return requestRepository.findById(reqId).orElseThrow(
                () -> new RequestNotFoundException(String.format("Request with id:%s not found", reqId))
        );
    }

    private void rejectAllPendingRequestByEvent(Event event) {
        getRequestsByEvent(event)
                .forEach((r) -> {
                    if (r.getStatus().equals(RequestStatus.PENDING)) {
                        r.setStatus(RequestStatus.REJECTED);
                    }
                });
    }

    private void isInitiator(Event event, long userId) {
        boolean isInitiator = event.getInitiator().getId().equals(userId);

        if (!isInitiator) {
            throw new UserIsNotInitiatorException(
                    String.format("User with id:%s is not the initiator of the event with id:%s", userId, event.getId())
            );
        }
    }

    private List<EventShortDto> getEventsWithoutSort(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            int from,
            int size
    ) {
        PageRequest request = PageRequest.of(from, size);

        List<Event> events = eventRepository.findAll(
                EventSpecs
                        .hasTextInAnnotationOrDescription(text)
                        .and(EventSpecs.hasEventCategory(categories))
                        .and(EventSpecs.isPaid(paid))
                        .and(EventSpecs.betweenDates(rangeStart, rangeEnd))
                        .and(EventSpecs.isEventAvailable(onlyAvailable)),
                request
        ).toList();

        return addViewsAndRequestsForEventShortDto(events);
    }

    private List<EventShortDto> getEventsSortByViews(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            int from,
            int size
    ) {
        List<Event> events = eventRepository.findAll(
                EventSpecs
                        .hasTextInAnnotationOrDescription(text)
                        .and(EventSpecs.hasEventCategory(categories))
                        .and(EventSpecs.isPaid(paid))
                        .and(EventSpecs.betweenDates(rangeStart, rangeEnd))
                        .and(EventSpecs.isEventAvailable(onlyAvailable))
        );

        List<EventShortDto> res = addViewsAndRequestsForEventShortDto(events);

        res.sort((Comparator.comparingInt(EventShortDto::getViews).reversed()));

        size = from + size;

        if (size > events.size()) {
            size = events.size();
        }

        return res.subList(from, size);
    }

    private List<EventShortDto> getEventsSortByEventDate(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            int from,
            int size
    ) {
        PageRequest request = PageRequest.of(from, size, Sort.by("eventDate"));

        List<Event> events = eventRepository.findAll(
                EventSpecs
                        .hasTextInAnnotationOrDescription(text)
                        .and(EventSpecs.hasEventCategory(categories))
                        .and(EventSpecs.isPaid(paid))
                        .and(EventSpecs.betweenDates(rangeStart, rangeEnd))
                        .and(EventSpecs.isEventAvailable(onlyAvailable)),
                request
        ).toList();

        return addViewsAndRequestsForEventShortDto(events);
    }

    private void addViewsAndRequests(Event event) {
        Set<String> uri = Set.of("/events/" + event.getId());
        LocalDateTime rangeStart = event.getCreatedOn();
        LocalDateTime rangeEnd = LocalDateTime.now();

        Optional<ViewStats> viewStats = statisticClient.getStats(rangeStart, rangeEnd, uri, false)
                .stream().findFirst();

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);

        viewStats.ifPresent(vs -> eventFullDto.setViews(vs.getHits()));

        eventFullDto.setConfirmedRequests(getConfirmedRequestsCountForEvent(event));
    }

    private List<EventShortDto> addViewsAndRequestsForEventShortDto(List<Event> events) {
        Map<String, ViewStats> viewStatsMap = new HashMap<>();

        List<EventShortDto> eventsShort = events.stream()
                .map(e -> {
                    viewStatsMap.put("/events/" + e.getId(), null);
                    EventShortDto shortDto = EventMapper.toEventShortDto(e);
                    shortDto.setConfirmedRequests(getConfirmedRequestsCountForEvent(e));
                    return shortDto;
                }).collect(Collectors.toList());

        eventsShort.forEach(e -> {
            String eventUrl = "/events/" + e.getId();
            ViewStats viewStats = putStats(events, viewStatsMap).get(eventUrl);

            if (viewStats != null) {
                e.setViews(viewStats.getHits());
            } else {
                e.setViews(0);
            }
        });

        return eventsShort;
    }

    private List<EventFullDto> addViewsAndRequestsForEventFullDto(List<Event> events) {
        Map<String, ViewStats> viewStatsMap = new HashMap<>();

        List<EventFullDto> eventsFull = events.stream()
                .map(e -> {
                    viewStatsMap.put("/events/" + e.getId(), null);
                    EventFullDto fullDto = EventMapper.toEventFullDto(e);
                    fullDto.setConfirmedRequests(getConfirmedRequestsCountForEvent(e));
                    return fullDto;
                }).collect(Collectors.toList());

        eventsFull.forEach(e -> {
            String eventUrl = "/events/" + e.getId();
            ViewStats viewStats = putStats(events, viewStatsMap).get(eventUrl);

            if (viewStats != null) {
                e.setViews(viewStats.getHits());
            } else {
                e.setViews(0);
            }
        });

        return eventsFull;
    }

    private Map<String, ViewStats> putStats(List<Event> events, Map<String, ViewStats> viewStatsMap) {
        statisticClient.getStats(
                getEventWithMinCreatedOn(events).getCreatedOn(),
                LocalDateTime.now(),
                viewStatsMap.keySet(),
                false
        ).forEach(vs -> viewStatsMap.put(vs.getUri(), vs));
        return viewStatsMap;
    }

    private long getConfirmedRequestsCountForEvent(Event event) {
        List<Request> requests = requestRepository.findByEvent(event);
        return requests.stream().filter(r -> r.getStatus().equals(RequestStatus.CONFIRMED)).count();
    }
}
