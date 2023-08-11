package ru.practicum.mainservice.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.constants.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewUserRequest {
    @Email
    @NotBlank
    @Size(min = Constants.MIN_LENGTH_EMAIL, max = Constants.MAX_LENGTH_EMAIL)
    String email;
    @NotBlank
    @Size(min = Constants.MIN_SIZE_NAME_OF_USER, max = Constants.MAX_SIZE_NAME_OF_USER)
    String name;
}