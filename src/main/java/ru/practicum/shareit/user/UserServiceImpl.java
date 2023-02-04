package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDto createUser(UserCreateDto user) {
        try {
            User userResponse = UserMapper.toUser(user);
            userRepository.save(userResponse);
            return UserMapper.toUserDto(userResponse);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Пользователь с " + user.getEmail() + " уже зарегистрирован.");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        checkUserId(userId);
        userUpdateDto.setId(userId);
        checkUserEmail(userUpdateDto.getEmail());

        User userFromRepository = userRepository.getById(userId);
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userUpdateDto, userFromRepository)));
    }

    @Override
    public List<UserDto> findAll() {
        List<User> list = userRepository.findAll();
        return UserMapper.mapToUserDto(list);
    }

    @Override
    public UserDto getUser(Long userId) {
        checkUserId(userId);
        User user = userRepository.getById(userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(Long userId) {
        checkUserId(userId);
        userRepository.deleteById(userId);
    }

    private void checkUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Пользователя с id " + userId + " не существует");
        }
    }

    private void checkUserEmail(String email) {
        if (!userRepository.getByEmail(email).isEmpty()) {
            throw new DataIntegrityViolationException("Пользователь с " + email + " уже зарегистрирован.");
        }
    }
}
