package ru.practicum.mainservice.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exception.InvalidUniqueKeyException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.user.dto.NewUserRequest;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.UserModel;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        PageRequest pageable = PageRequest.of(from / size, size);
        log.info("АДМИН - Список пользователей");
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllByIdIn(ids, pageable).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        try {
            log.info("АДМИН - Пользователь {} создан", newUserRequest);
            return UserMapper.toUserDto(userRepository.save(UserMapper.fromNewUserRequestToModel(newUserRequest)));
        } catch (DataIntegrityViolationException exception) {
            throw new InvalidUniqueKeyException("Пользователь с именем " + newUserRequest.getName() + " уже существует");
        }
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        log.info("АДМИН - Пользователь с id {} удален", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public UserModel getUserById(Long userId) {
        log.info("АДМИН - Запрос на поиск пользователя с id {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
