package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDto createUser(User user) {
        User userResponse = userRepository.save(user);
        return UserMapper.toUserDto(userResponse);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDto updateUser(Long userId, UserUpdateDto userUpdateDto) {

        userUpdateDto.setId(userId);
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
        User user = userRepository.getById(userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
