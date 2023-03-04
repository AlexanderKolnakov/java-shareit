package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping()
    public UserDto createUser(@RequestBody UserCreateDto user) {
        log.debug("Получен POST запрос на создание пользователя");
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.debug("Получен PATCH запрос на обновление пользователя пользователя с id: " + userId);
        return userService.updateUser(userId, userDto);
    }

    @GetMapping
    public List<UserDto> findAllUsers() {
        log.debug("Получен GET запрос на получение списка всех пользователей.");
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto findUser(@PathVariable Long userId) {
        log.debug("Получен GET запрос на получение пользователя с id: {}.", userId);
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.debug("Получен DELETE запрос на удаление пользователя с id: {}.", userId);
        userService.deleteUser(userId);
    }
}
