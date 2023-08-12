package ru.practicum.mainservice.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.constants.Constants;
import ru.practicum.mainservice.event.dto.EventShortDto;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentShortDto {
    Long id;
    String commentText;
    @JsonFormat(pattern = Constants.DATETIME_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime createdOn;
    EventShortDto event;
}
