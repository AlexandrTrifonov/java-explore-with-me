package ru.practicum.mainservice.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.constants.Constants;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.user.dto.UserShortDto;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    Long id;
    String commentText;
    @JsonFormat(pattern = Constants.DATETIME_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime createdOn;
    UserShortDto author;
    EventShortDto event;
}
