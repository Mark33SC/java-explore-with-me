package ru.practicum.service.request;

import ru.practicum.service.request.dto.RequestFullDto;

import java.util.List;

public interface RequestService {

    RequestFullDto addRequest(long userId, long eventId);

    List<RequestFullDto> getRequestsFromCurrentUser(long userId);

    RequestFullDto cancelRequestCurrentUser(long userId, long requestId);
}