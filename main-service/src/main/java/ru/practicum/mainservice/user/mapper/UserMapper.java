package ru.practicum.mainservice.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.user.dto.NewUserRequest;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.dto.UserShortDto;
import ru.practicum.mainservice.user.model.UserModel;

@UtilityClass
public class UserMapper {

    public static UserDto toUserDto(UserModel userModel) {
        return UserDto.builder()
                .email(userModel.getEmail())
                .id(userModel.getId())
                .name(userModel.getName())
                .build();
    }

    public static UserModel fromNewUserRequestToModel(NewUserRequest newUserRequest) {
        return UserModel.builder()
                .email(newUserRequest.getEmail())
                .name(newUserRequest.getName())
                .build();
    }

    public static UserShortDto toUserShortDto(UserModel userModel) {
        return UserShortDto.builder()
                .id(userModel.getId())
                .name(userModel.getName())
                .build();
    }
}
