package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(Long userId, User user);

    List<User> findAll();

    User getUser(Long userId);

    void deleteUser(Long userId);
}
