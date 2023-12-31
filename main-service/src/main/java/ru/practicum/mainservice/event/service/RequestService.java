package ru.practicum.mainservice.event.service;

import ru.practicum.mainservice.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.event.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getEventRequestsByEventOwner(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequestsByEventOwner(
            Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<ParticipationRequestDto> getEventRequestsByRequester(Long userId);

    ParticipationRequestDto createEventRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelEventRequest(Long userId, Long requestId);
}
