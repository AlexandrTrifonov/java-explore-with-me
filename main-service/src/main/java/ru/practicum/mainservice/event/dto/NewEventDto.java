package ru.practicum.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.constants.Constants;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {
    @NotBlank
    @Size(min = Constants.MIN_LENGTH_ANNOTATION, max = Constants.MAX_LENGTH_ANNOTATION)
    String annotation;
    @NotNull
    Long category;
    @NotBlank
    @Size(min = Constants.MIN_LENGTH_DESCRIPTION, max = Constants.MAX_LENGTH_DESCRIPTION)
    String description;
    @NotNull
    @Future
    @JsonFormat(pattern = Constants.DATETIME_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime eventDate;
    @NotNull
    @Valid
    LocationDto location;
    boolean paid = false;
    @PositiveOrZero
    long participantLimit = 0L;
    boolean requestModeration = true;
    @NotBlank
    @Size(min = Constants.MIN_LENGTH_TITLE, max = Constants.MAX_LENGTH_TITLE)
    String title;

    public Boolean getPaid() {
        return Boolean.parseBoolean(String.valueOf(paid));
    }

    public Long getParticipantLimit() {
        return Long.parseLong(String.valueOf(participantLimit));
    }

    public Boolean getRequestModeration() {
        return Boolean.parseBoolean(String.valueOf(requestModeration));
    }
}
