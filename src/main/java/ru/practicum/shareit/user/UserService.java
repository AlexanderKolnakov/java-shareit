package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserCreateDto user);

    UserDto updateUser(Long userId, UserUpdateDto userUpdateDto);

    List<UserDto> findAll();

    UserDto getUser(Long userId);

    void deleteUser(Long userId);
}
