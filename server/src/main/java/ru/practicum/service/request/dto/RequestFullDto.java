package ru.practicum.service.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.request.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestFullDto {

    private Long id;

    private Long event;

    private Long requester;

    private RequestStatus status;

    private LocalDateTime created;

}