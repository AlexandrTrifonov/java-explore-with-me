package ru.practicum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.event.dto.ParticipationRequestDto;
import ru.practicum.mainservice.event.enums.RequestStatus;
import ru.practicum.mainservice.event.enums.RequestStatusAction;
import ru.practicum.mainservice.event.enums.State;
import ru.practicum.mainservice.event.mapper.RequestMapper;
import ru.practicum.mainservice.event.model.EventModel;
import ru.practicum.mainservice.event.model.RequestModel;
import ru.practicum.mainservice.event.repository.RequestRepository;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.exception.ValidationException;
import ru.practicum.mainservice.exception.LimitRequestException;
import ru.practicum.mainservice.user.model.UserModel;
import ru.practicum.mainservice.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final EventService eventService;
    private final RequestRepository requestRepository;
    private final StatsService statsService;

    @Override
    public List<ParticipationRequestDto> getEventRequestsByEventOwner(Long userId, Long eventId) {
        userService.getUserById(userId);
        EventModel event = eventService.getEventById(eventId);
        checkUserIsOwner(event.getInitiator().getId(), userId);
        return listParticipationRequestsDto(requestRepository.findAllByEventId(eventId));
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestsByEventOwner(
            Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userService.getUserById(userId);
        EventModel event = eventService.getEventById(eventId);
        checkUserIsOwner(event.getInitiator().getId(), userId);
        if (!event.getRequestModeration() ||
                event.getParticipantLimit() == 0 ||
                eventRequestStatusUpdateRequest.getRequestIds().isEmpty()) {
            return new EventRequestStatusUpdateResult(List.of(), List.of());
        }
        List<RequestModel> confirmedList = new ArrayList<>();
        List<RequestModel> rejectedList = new ArrayList<>();
        List<RequestModel> requests = requestRepository.findAllByIdIn(eventRequestStatusUpdateRequest.getRequestIds());
        if (!requests.stream()
                .map(RequestModel::getStatus)
                .allMatch(RequestStatus.PENDING::equals)) {
            throw new ValidationException("Изменять можно только заявки, находящиеся в ожидании.");
        }
        if (eventRequestStatusUpdateRequest.getStatus().equals(RequestStatusAction.REJECTED)) {
            rejectedList.addAll(changeStatusAndSave(requests, RequestStatus.REJECTED));
        } else {
            Long newConfirmedRequests = statsService.getConfirmedRequests(List.of(event)).getOrDefault(eventId, 0L) +
                    eventRequestStatusUpdateRequest.getRequestIds().size();

            checkIsNewLimitGreaterOld(newConfirmedRequests, event.getParticipantLimit());

            confirmedList.addAll(changeStatusAndSave(requests, RequestStatus.CONFIRMED));

            if (newConfirmedRequests >= event.getParticipantLimit()) {
                rejectedList.addAll(changeStatusAndSave(
                        requestRepository.findAllByEventIdAndStatus(eventId, RequestStatus.PENDING),
                        RequestStatus.REJECTED)
                );
            }
        }
        return new EventRequestStatusUpdateResult(listParticipationRequestsDto(confirmedList),
                listParticipationRequestsDto(rejectedList));
    }

    @Override
    public List<ParticipationRequestDto> getEventRequestsByRequester(Long userId) {
        userService.getUserById(userId);
        return toParticipationRequestsDto(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public ParticipationRequestDto createEventRequest(Long userId, Long eventId) {
        UserModel user = userService.getUserById(userId);
        EventModel event = eventService.getEventById(eventId);
        if (userId.equals(event.getInitiator().getId())) {
            throw new ValidationException("Нельзя создавать запрос на собственное событие.");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Нельзя создавать запрос на неопубликованное событие.");
        }
        Optional<RequestModel> checkRepeatRequest = requestRepository.findByEventIdAndRequesterId(eventId, userId);
        if (checkRepeatRequest.isPresent()) {
            throw new ValidationException("Нельзя создавать повторный запрос.");
        }
        checkLimitRequests(
                statsService.getConfirmedRequests(List.of(event)).getOrDefault(eventId, 0L),
                event.getParticipantLimit()
        );
        RequestModel newRequest = RequestModel.builder()
                .event(event)
                .requester(user)
                .created(LocalDateTime.now())
                .build();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            newRequest.setStatus(RequestStatus.CONFIRMED);
        } else {
            newRequest.setStatus(RequestStatus.PENDING);
        }
        return RequestMapper.toParticipationRequestDto(requestRepository.save(newRequest));
    }

    @Override
    public ParticipationRequestDto cancelEventRequest(Long userId, Long requestId) {
        userService.getUserById(userId);
        RequestModel request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Заявки на участие не существует."));
        checkUserIsOwner(request.getRequester().getId(), userId);
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    private void checkUserIsOwner(Long id, Long userId) {
        if (!id.equals(userId)) {
            throw new ValidationException("Пользователь не является инициатором собятия.");
        }
    }

    private List<ParticipationRequestDto> listParticipationRequestsDto(List<RequestModel> requests) {
        return requests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    private List<ParticipationRequestDto> toParticipationRequestsDto(List<RequestModel> requests) {
        return requests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    private void checkLimitRequests(Long newLimit, Long eventParticipantLimit) {
        if (eventParticipantLimit != 0 && (newLimit >= eventParticipantLimit)) {
            throw new ValidationException(String.format("Достигнут лимит подтвержденных запросов на участие: %d",
                    eventParticipantLimit));
        }
    }

    private List<RequestModel> changeStatusAndSave(List<RequestModel> requests, RequestStatus status) {
        requests.forEach(request -> request.setStatus(status));
        return requestRepository.saveAll(requests);
    }

    private void checkIsNewLimitGreaterOld(Long newLimit, Long eventParticipantLimit) {
        if (eventParticipantLimit != 0 && (newLimit > eventParticipantLimit)) {
            throw new LimitRequestException("Достигнут лимит подтвержденных запросов на участие: %d");
        }
    }
}
