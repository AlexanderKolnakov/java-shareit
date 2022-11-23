package ru.practicum.shareit.user;

import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.model.User;

import java.util.List;

public interface UserService {
    UserDto createUser(User user);

    UserDto updateUser(Long userId, User user);

    List<UserDto> findAll();

    UserDto getUser(Long userId);

    void deleteUser(Long userId);
}
