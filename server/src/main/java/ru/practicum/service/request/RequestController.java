package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.request.dto.RequestFullDto;

import java.util.List;

@RestController
@RequestMapping(path = "users")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    public List<RequestFullDto> getRequestFromCurrentUserToEventOtherUsers(@PathVariable long userId) {
        return requestService.getRequestsFromCurrentUser(userId);
    }

    @PostMapping("/{userId}/requests")
    public RequestFullDto addRequestFromCurrentUser(@PathVariable long userId, @RequestParam long eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestFullDto cancelRequestCurrentUser(@PathVariable long userId, @PathVariable long requestId) {
        return requestService.cancelRequestCurrentUser(userId, requestId);
    }
}
