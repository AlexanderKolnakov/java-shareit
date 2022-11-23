package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.user.UserDao;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public UserDto createUser(User user) {
        return userDao.createUser(user);
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        return userDao.updateUser(userId, user);
    }

    @Override
    public List<UserDto> findAll() {
        return userDao.findAllUsers();
    }

    @Override
    public UserDto getUser(Long userId) {
        return userDao.getUser(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        userDao.deleteUser(userId);
    }
}
