package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.model.User;

@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }
}


