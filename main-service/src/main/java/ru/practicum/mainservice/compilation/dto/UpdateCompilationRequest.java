package ru.practicum.mainservice.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.constants.Constants;

import javax.validation.constraints.Size;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCompilationRequest {
    Set<Long> events;
    Boolean pinned;
    @Size(min = Constants.MIN_SIZE_TITLE_OF_COMPILATION, max = Constants.MAX_SIZE_TITLE_OF_COMPILATION)
    String title;
}
