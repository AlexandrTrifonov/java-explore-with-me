package ru.practicum.mainservice.comment.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class Count {
    Long eventId;
    Long count;

    public Count(Long eventId, Long count) {
        this.eventId = eventId;
        this.count = count;
    }
}
