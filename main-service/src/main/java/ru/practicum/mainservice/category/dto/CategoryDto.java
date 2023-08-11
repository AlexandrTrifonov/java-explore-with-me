package ru.practicum.mainservice.category.dto;

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
public class CategoryDto {
    Long id;
    @NotBlank
    @Size(min = Constants.MIN_SIZE_NAME_OF_CATEGORY, max = Constants.MAX_SIZE_NAME_OF_CATEGORY)
    String name;
}
