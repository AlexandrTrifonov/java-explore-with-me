package ru.practicum.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.constants.Constants;
import ru.practicum.mainservice.event.enums.EventStateAction;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEventAdminRequest {
    @Size(min = Constants.MIN_LENGTH_ANNOTATION, max = Constants.MAX_LENGTH_ANNOTATION)
    String annotation;
    Long category;
    @Size(min = Constants.MIN_LENGTH_DESCRIPTION, max = Constants.MAX_LENGTH_DESCRIPTION)
    String description;
    @JsonFormat(pattern = Constants.DATETIME_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime eventDate;
    @Valid
    LocationDto location;
    Boolean paid;
    @PositiveOrZero
    Long participantLimit;
    Boolean requestModeration;
    EventStateAction stateAction;
    @Size(min = Constants.MIN_LENGTH_TITLE, max = Constants.MAX_LENGTH_TITLE)
    String title;
}
