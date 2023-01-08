package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static List<UserDto> mapToUserDto(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(toUserDto(user));
        }
        return result;
    }

    public static User toUser(UserUpdateDto userUpdateDto, User userFromRepository) {
        User user =  new User();
        user.setId(userUpdateDto.getId());
        if (userUpdateDto.getName() != null) {
            user.setName(userUpdateDto.getName());
        } else {
            user.setName(userFromRepository.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            user.setEmail(userUpdateDto.getEmail());
        } else {
            user.setEmail(userFromRepository.getEmail());
        }
        return user;
    }
}


