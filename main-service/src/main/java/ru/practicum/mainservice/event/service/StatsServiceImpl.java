package ru.practicum.mainservice.event.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mainservice.constants.Constants;
import ru.practicum.mainservice.event.model.EventModel;
import ru.practicum.mainservice.event.repository.RequestRepository;
import ru.practicum.mainservice.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsClient statsClient;
    private final RequestRepository requestRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value(value = "${app}")
    private String app;

    @Override
    public void addHit(HttpServletRequest request) {
        statsClient.saveHit(app,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.parse(LocalDateTime.now().format(Constants.DATETIME_FORMATTER), Constants.DATETIME_FORMATTER));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        ResponseEntity<Object> response = statsClient.getStats(start, end, uris, unique);
        try {
            List<ViewStatsDto> result = objectMapper.convertValue(
                    response.getBody(), new TypeReference<List<ViewStatsDto>>() {});
            return result;
        } catch (RuntimeException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    @Override
    public Map<Long, Long> getViews(List<EventModel> events) {
        Map<Long, Long> views = new HashMap<>();
        log.info("Вызов метода получения статистики просмотров");
        List<EventModel> publishedEvents = getPublished(events);
        if (events.isEmpty()) {
            return views;
        }
        Optional<LocalDateTime> minPublishedOn = publishedEvents.stream()
                .map(EventModel::getPublishedOn)
                .min(LocalDateTime::compareTo);

        if (minPublishedOn.isPresent()) {
            LocalDateTime start = minPublishedOn.get();
            LocalDateTime end = LocalDateTime.now();
            List<String> uris = publishedEvents.stream()
                    .map(EventModel::getId)
                    .map(id -> ("/events/" + id))
                    .collect(Collectors.toList());

            List<ViewStatsDto> stats = getStats(start, end, uris, null);
            stats.forEach(stat -> {
                Long eventId = Long.parseLong(stat.getUri()
                        .split("/", 0)[2]);
                views.put(eventId, views.getOrDefault(eventId, 0L) + stat.getHits());
            });
        }
        return views;
    }

    @Override
    public Map<Long, Long> getConfirmedRequests(List<EventModel> events) {
        log.info("Вызов метода получения статистики одобренных заявок");
        List<Long> eventsId = getPublished(events).stream()
                .map(EventModel::getId)
                .collect(Collectors.toList());
        Map<Long, Long> requestStats = new HashMap<>();
        if (!eventsId.isEmpty()) {
            requestRepository.getConfirmedRequests(eventsId)
                    .forEach(stat -> requestStats.put(stat.getEventId(), stat.getConfirmedRequests()));
        }

        return requestStats;
    }

    private List<EventModel> getPublished(List<EventModel> events) {
        return events.stream()
                .filter(event -> event.getPublishedOn() != null)
                .collect(Collectors.toList());
    }
}
