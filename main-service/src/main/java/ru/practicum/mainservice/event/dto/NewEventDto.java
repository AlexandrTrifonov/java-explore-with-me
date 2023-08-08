package ru.practicum.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.constants.Constants;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
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
    @JsonFormat(pattern = Constants.DATETIME_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime eventDate;
    @NotNull
    @Valid
    LocationDto location;
    Boolean paid = false;
    @PositiveOrZero
    Long participantLimit = 0L;
    Boolean requestModeration = true;
    @NotBlank
    @Size(min = Constants.MIN_LENGTH_TITLE, max = Constants.MAX_LENGTH_TITLE)
    String title;
}
