package ru.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.constants.Constants;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.event.enums.State;
import ru.practicum.mainservice.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsByAdmin(
        @RequestParam(required = false) List<Long> users,
        @RequestParam(required = false) List<State> states,
        @RequestParam(required = false) List<Long> categories,
        @RequestParam(defaultValue = Constants.RANGE_START)
        @DateTimeFormat(pattern = Constants.DATETIME_FORMAT) LocalDateTime rangeStart,
        @RequestParam(defaultValue = Constants.RANGE_END)
        @DateTimeFormat(pattern = Constants.DATETIME_FORMAT) LocalDateTime rangeEnd,
        @RequestParam(required = false, defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
        @RequestParam(required = false, defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        log.info("Контроллер getEventsByAdmin");
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto patchEventByAdmin(@PathVariable Long eventId,
                                          @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequest);
    }
}
