package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.UserCreateException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserDaoImpl implements UserDao {

    private final Map<Long, User> users = new HashMap<>();
    private long usersId = 1;

    @Override
    public User createUser(User user) {
        checkEmail(user);
        generateID(user);
        users.put(user.getId(), user);
        log.debug("Пользователь с id {} и email {} успешно добавлен.", user.getId(), user.getEmail());
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        checkUserID(userId);
        checkEmail(user);
        User updateUser = users.get(userId);

        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());

        }
        users.put(userId, updateUser);
        return updateUser;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long userId) {
        return users.get(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        checkUserID(userId);
        users.remove(userId);
    }

    private void generateID(User user) {
        user.setId(usersId);
        usersId++;
    }

    private void checkEmail(User user) {
        for (User newUser : users.values()) {
            if (newUser.getEmail().equals(user.getEmail())) {
                throw new UserCreateException("Пользователь с email " + user.getEmail() + " уже зарегистрирован.");
            }
        }
    }

    @Override
    public void checkUserID(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователя с id " + id + " не существует.");
        }
    }
}
