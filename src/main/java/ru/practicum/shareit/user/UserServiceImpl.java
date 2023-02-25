package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
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
            throw new ConflictException("Пользователь с " + user.getEmail() + " уже зарегистрирован.");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDto updateUser(Long userId, UserDto userUpdateDto) {
        userUpdateDto.setId(userId);

        final User userFromRepository = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователя с id " + userId + " не существует"));
        updateUserInfo(userUpdateDto, userFromRepository);
        return UserMapper.toUserDto(userRepository.save(userFromRepository));
    }


    @Override
    public List<UserDto> findAll() {
        List<User> list = userRepository.findAll();
        return UserMapper.mapToUserDto(list);
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователя с id " + userId + " не существует"));
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователя с id " + userId + " не существует"));
        userRepository.deleteById(userId);
    }


    private void checkUserEmail(String email) {
        if (!userRepository.getByEmail(email).isEmpty()) {
            throw new ConflictException("Пользователь с " + email + " уже зарегистрирован.");
        }
    }

    private void updateUserInfo(UserDto userUpdateDto, User userFromRepository) {
        if (userUpdateDto.getName() != null) {
            userFromRepository.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            userFromRepository.setEmail(userUpdateDto.getEmail());
        }
    }
}
