package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    User createUser(User user);

    User updateUser(Long userId, User user);

    List<User> findAllUsers();

    User getUser(Long userId);

    void deleteUser(Long userId);

    void checkUserID(Long id);
}
