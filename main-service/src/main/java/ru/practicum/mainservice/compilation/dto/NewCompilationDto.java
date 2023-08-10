package ru.practicum.mainservice.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.constants.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCompilationDto {
    Set<Long> events;
    @NotNull
    Boolean pinned = false;
    @NotBlank
    @Size(min = Constants.MIN_SIZE_TITLE_OF_COMPILATION, max = Constants.MAX_SIZE_TITLE_OF_COMPILATION)
    String title;
}
