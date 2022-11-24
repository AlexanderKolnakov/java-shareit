package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.UserCreateException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserDaoImpl implements UserDao {

    private final Map<Long, User> users = new HashMap<>();
    private long usersId = 1;

    @Override
    public UserDto createUser(User user) {
        checkEmail(user);
        generateID(user);
        users.put(user.getId(), user);
        log.debug("Пользователь с id {} и email {} успешно добавлен.", user.getId(), user.getEmail());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
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
        return UserMapper.toUserDto(updateUser);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return new ArrayList<>(users.values()).stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long userId) {
        checkUserID(userId);
        return UserMapper.toUserDto(users.get(userId));
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
