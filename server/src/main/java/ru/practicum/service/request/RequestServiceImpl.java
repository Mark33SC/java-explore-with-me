package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.event.Event;
import ru.practicum.service.event.EventRepository;
import ru.practicum.service.event.EventState;
import ru.practicum.service.exception.event.EventNotFoundException;
import ru.practicum.service.exception.request.*;
import ru.practicum.service.request.dto.RequestFullDto;
import ru.practicum.service.request.dto.RequestMapper;
import ru.practicum.service.user.User;
import ru.practicum.service.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    private final UserService userService;

    @Override
    public RequestFullDto addRequest(long userId, long eventId) {
        Event event = findEventById(eventId);
        User user = userService.getUserById(userId);

        checkRequestLimit(event);
        isPublished(event);
        isInitiator(user, event);
        isNotAlreadyExist(userId, eventId);

        Request newRequest = new Request();
        newRequest.setEvent(event);
        newRequest.setRequester(user);
        newRequest.setCreated(LocalDateTime.now());

        if (!event.isRequestModeration()) {
            newRequest.setStatus(RequestStatus.CONFIRMED);
        } else {
            newRequest.setStatus(RequestStatus.PENDING);
        }

        return RequestMapper.toRequestFullDto(requestRepository.save(newRequest));
    }

    @Override
    public List<RequestFullDto> getRequestsFromCurrentUser(long userId) {
        log.info("Get request from current user with id:{}", userId);

        User requester = userService.getUserById(userId);

        return requestRepository
                .findAllByRequester(requester)
                .stream()
                .map(RequestMapper::toRequestFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestFullDto cancelRequestCurrentUser(long userId, long requestId) {
        User user = userService.getUserById(userId);
        Request request = findRequestById(requestId);

        if (!request.getRequester().equals(user)) {
            throw new UserNotRequesterException(
                    String.format("User with id:%s does not requester for request with id:%s", userId, requestId)
            );
        }

        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.toRequestFullDto(request);
    }

    private void isNotAlreadyExist(long userId, long eventId) {
        Optional<RequestFullDto> foundDuplicate = getRequestsFromCurrentUser(userId)
                .stream()
                .filter(r -> Objects.equals(r.getEvent(), eventId))
                .findFirst();

        if (foundDuplicate.isPresent()) {
            throw new RequestAlreadyExistException(
                    String.format("Request for event with id:%s on user with id:%s already exist", eventId, userId)
            );
        }
    }

    private void isInitiator(User requester, Event event) {
        boolean isEventInitiator = event.getInitiator().getId().equals(requester.getId());

        if (isEventInitiator) {
            throw new RequesterIsInitiatorEventException("Requester cannot be initiator of event");
        }
    }

    private void isPublished(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestUnpublishedEventException("Request to an unpublished event is prohibited");
        }
    }

    private void checkRequestLimit(Event event) {
        int requestCount = requestRepository.findAllByEvent(event).size();
        if (event.getParticipantLimit() > 0 && requestCount >= event.getParticipantLimit()) {
            throw new ParticipantLimitExceededException(
                    String.format("Participant limit exceeded from event with id:%s", event.getId())
            );
        }
    }

    private Event findEventById(long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new EventNotFoundException(String.format("Event with id:%s not found", id))
        );
    }

    private Request findRequestById(long id) {
        return requestRepository.findById(id).orElseThrow(
                () -> new RequestNotFoundException(String.format("Request with id:%s not found", id))
        );
    }
}
