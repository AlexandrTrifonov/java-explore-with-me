package ru.practicum.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.constants.Constants;
import ru.practicum.mainservice.event.enums.StateUserAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEventUserRequest {
    @Size(min = Constants.MIN_LENGTH_ANNOTATION, max = Constants.MAX_LENGTH_ANNOTATION)
    String annotation;
    Long category;
    @Size(min = Constants.MIN_LENGTH_DESCRIPTION, max = Constants.MAX_LENGTH_DESCRIPTION)
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    LocalDateTime eventDate;
    LocationDto location;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
    StateUserAction stateAction;
    @Size(min = Constants.MIN_LENGTH_TITLE, max = Constants.MAX_LENGTH_TITLE)
    String title;
}
