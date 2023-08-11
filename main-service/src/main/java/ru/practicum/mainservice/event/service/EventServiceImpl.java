package ru.practicum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.CategoryModel;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.enums.EventSortType;
import ru.practicum.mainservice.event.enums.EventStateAction;
import ru.practicum.mainservice.event.enums.State;
import ru.practicum.mainservice.event.enums.StateUserAction;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.mapper.LocationMapper;
import ru.practicum.mainservice.event.model.EventModel;
import ru.practicum.mainservice.event.model.LocationModel;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.*;
import ru.practicum.mainservice.user.model.UserModel;
import ru.practicum.mainservice.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final StatsService statsService;

    @Override
    public List<EventShortDto> getEventsPrivate(Long userId, int from, int size) {
        userService.getUserById(userId);
        PageRequest pageable = PageRequest.of(from / size, size);
        List<EventModel> events = eventRepository.findAllByInitiatorId(userId, pageable);
        log.info("PRIVATE - Список событий пользователя с id {}", userId);
        return getListEventsShortDto(events);
    }

    @Override
    public EventFullDto createEventPrivate(Long userId, NewEventDto newEventDto) {
        validateNewEventDate(newEventDto.getEventDate());
        UserModel eventUser = userService.getUserById(userId);
        CategoryModel eventCategory = categoryService.getCategoryModelById(newEventDto.getCategory());
        LocationModel eventLocation = LocationMapper.toLocationModel(newEventDto.getLocation());
        EventModel newEventModel = EventMapper.toEventModel(newEventDto, eventCategory, eventLocation, eventUser);
        eventRepository.save(newEventModel);
        log.info("PRIVATE - Создано новое событие {}", newEventModel);
        return EventMapper.toEventFullDto(newEventModel, 0L, 0L);
    }

    @Override
    public EventFullDto getEventByPrivate(Long userId, Long eventId) {
        userService.getUserById(userId);
        EventModel event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        log.info("PRIVATE - Событие {} пользователя с id {}", eventId, userId);
        return EventMapper.toEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId), 0L, 0L);
    }

    @Override
    public EventFullDto updateEventByPrivate(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        log.info("Запуск метода updateEventByPrivate");
        validateNewEventDate(updateEventUserRequest.getEventDate());
        userService.getUserById(userId);
        EventModel event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new InvalidStateEventException("Данное событие не может быть изменено " +
                    "(изменить можно только отмененные события или события в состоянии ожидания модерации)");
        }
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(StateUserAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PENDING);
            }
        }
        if (updateEventUserRequest.getAnnotation() != null && !updateEventUserRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null) {
            event.setCategoryModel(CategoryMapper.fromCategoryDtotoModel(categoryService.getCategoryById(updateEventUserRequest.getCategory())));
        }
        if (updateEventUserRequest.getDescription() != null && !updateEventUserRequest.getDescription().isBlank()) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLocation(LocationMapper.toLocationModel(updateEventUserRequest.getLocation()));
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getTitle() != null && !updateEventUserRequest.getTitle().isBlank()) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        eventRepository.save(event);
        log.info("PRIVATE - Обновление события {}", eventId);
        return getListEventsFullDto(List.of(event)).get(0);
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<State> states, List<Long> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        validateRangeStartEnd(rangeStart, rangeEnd);
        Pageable pageable = PageRequest.of(from, size);
        Page<EventModel> eventsPage = eventRepository.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, pageable);
        List<EventModel> events = eventsPage.getContent();
        log.info("ADMIN - Получение списка событий");
        return getListEventsFullDto(events);
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Сервис updateEventByAdmin, событие {}", eventId);
        validateUpdateEventDate(updateEventAdminRequest.getEventDate());
        EventModel event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События не существует."));
        if (updateEventAdminRequest.getAnnotation() != null && !updateEventAdminRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getDescription() != null && !updateEventAdminRequest.getDescription().isBlank()) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategoryModel(categoryService.getCategoryModelById(updateEventAdminRequest.getCategory()));
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(LocationMapper.toLocationModel(updateEventAdminRequest.getLocation()));
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction() == EventStateAction.PUBLISH_EVENT) {
                if (event.getState() == State.PENDING) {
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else {
                    log.warn("Cобытие можно публиковать, если оно в статусе ожидания публикации.");
                    throw new ValidationException("Cобытие можно публиковать, если оно в статусе ожидания публикации.");
                }
            }
            if (updateEventAdminRequest.getStateAction() == EventStateAction.REJECT_EVENT) {
                if (event.getState() != State.PUBLISHED) {
                    event.setState(State.CANCELED);
                } else {
                    log.warn("Cобытие можно отклонить, если оно еще не опубликовано.");
                    throw new ValidationException("Cобытие можно отклонить, если оно еще не опубликовано.");
                }
            }
        }
        if (updateEventAdminRequest.getTitle() != null && !updateEventAdminRequest.getTitle().isBlank()) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        log.info("ADMIN - Обновление события");
        eventRepository.save(event);
        return getListEventsFullDto(List.of(event)).get(0);
    }


    @Override
    public List<EventModel> getEventsByIdsList(List<Long> ids) {
        log.info("Получение списка событий по списку ids");
        return eventRepository.findAllByIdIn(ids);
    }

    @Override
    public List<EventShortDto> getListEventsShortDto(List<EventModel> events) {
        Map<Long, Long> views = statsService.getViews(events);
        Map<Long, Long> confirmedRequests = statsService.getConfirmedRequests(events);

        return events.stream()
                .map((event) -> EventMapper.toEventShortDto(
                        event,
                        confirmedRequests.getOrDefault(event.getId(), 0L),
                        views.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    public List<EventFullDto> getListEventsFullDto(List<EventModel> events) {
        Map<Long, Long> views = statsService.getViews(events);
        Map<Long, Long> confirmedRequests = statsService.getConfirmedRequests(events);

        return events.stream()
                .map((event) -> EventMapper.toEventFullDto(
                        event,
                        confirmedRequests.getOrDefault(event.getId(), 0L),
                        views.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    @Override
    public EventModel getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События не существует."));
    }

    @Override
    public List<EventShortDto> getEventsByPublic(String text, List<Long> categories, Boolean paid,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                 EventSortType sort, Integer from, Integer size, HttpServletRequest request) {
        log.info("Сервис getEventsByPublic");
        if (rangeStart == null || rangeEnd == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = rangeStart.plusYears(1);
        }
        validateRangeStartEnd(rangeStart, rangeEnd);
        Pageable pageable = PageRequest.of(from, size);
        Page<EventModel> eventsPage = eventRepository.getEventsByPublic(text, categories, paid, rangeStart, rangeEnd, pageable);
        List<EventModel> events = eventsPage.getContent();
        if (events.isEmpty()) {
            return List.of();
        }
        Map<Long, Long> eventsParticipantLimit = new HashMap<>();
        events.forEach(event -> eventsParticipantLimit.put(event.getId(), event.getParticipantLimit()));
        List<EventShortDto> eventsShortDto = getListEventsShortDto(events);
        if (onlyAvailable) {
            eventsShortDto = eventsShortDto.stream()
                    .filter(eventShort -> (eventsParticipantLimit.get(eventShort.getId()) == 0 ||
                            eventsParticipantLimit.get(eventShort.getId()) > eventShort.getConfirmedRequests()))
                    .collect(Collectors.toList());
        }
        if (sort != null && sort.equals(EventSortType.VIEWS)) {
            eventsShortDto.sort(Comparator.comparing(EventShortDto::getViews));
        } else if (sort != null && sort.equals(EventSortType.EVENT_DATE)) {
            eventsShortDto.sort(Comparator.comparing(EventShortDto::getEventDate));
        }
        statsService.addHit(request);
        return eventsShortDto;
    }

    @Override
    public EventFullDto getEventByPublic(Long eventId, HttpServletRequest request) {
        EventModel event = eventRepository.findByIdAndState(eventId, State.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("Событие не опубликовано.");
        }
        statsService.addHit(request);
        List<ViewStatsDto> stats = statsService.getStats(
                event.getPublishedOn(), LocalDateTime.now(), List.of(request.getRequestURI()), true);
        return EventMapper.toEventFullDto(event, null, stats.get(0).getHits());
    }

    private void validateNewEventDate(LocalDateTime eventDate) {
        LocalDateTime checkDate = LocalDateTime.now().plusHours(2);
        if (eventDate != null && eventDate.isBefore(checkDate)) {
            throw new InvalidEventDateException("Начало события ближе, чем 2 часа от текущего момента");
        }
    }

    private void validateRangeStartEnd(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new InvalidRangeStartEndException("Начало события позже окончания события");
        }
    }

    private void validateUpdateEventDate(LocalDateTime eventDate) {
        LocalDateTime checkDate = LocalDateTime.now().plusHours(1);
        if (eventDate != null && eventDate.isBefore(checkDate)) {
            throw new InvalidEventDateException("Начало редактируемого события ближе, чем 1 час от даты публикации");
        }
    }
}
