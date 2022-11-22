package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User createUser(User user) {
        return userDao.createUser(user);
    }

    @Override
    public User updateUser(Long userId, User user) {
        return userDao.updateUser(userId, user);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAllUsers();
    }

    @Override
    public User getUser(Long userId) {
        return userDao.getUser(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        userDao.deleteUser(userId);
    }
}
