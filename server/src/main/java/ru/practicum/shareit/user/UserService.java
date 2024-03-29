package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserCreateDto user);

    UserDto updateUser(Long userId, UserDto userUpdateDto) throws ConflictException;

    List<UserDto> findAll();

    UserDto getUser(Long userId);

    void deleteUser(Long userId);
}
