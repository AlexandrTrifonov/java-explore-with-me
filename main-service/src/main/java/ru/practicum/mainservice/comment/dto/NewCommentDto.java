package ru.practicum.mainservice.comment.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.constants.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCommentDto {
    @NotBlank
    @Size(min = Constants.MIN_LENGTH_COMMENT_TEXT, max = Constants.MAX_LENGTH_COMMENT_TEXT)
    String commentText;
}
