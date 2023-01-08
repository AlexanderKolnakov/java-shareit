package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    UserDto createUser(User user);

    UserDto updateUser(Long userId, User user);

    List<UserDto> findAllUsers();

    UserDto getUser(Long userId);

    void deleteUser(Long userId);

    void checkUserID(Long id);
}
