package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping()
    public User createUser(@RequestBody @Valid User user) {
        log.debug("Получен POST запрос на создание пользователя");
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User user) {
        log.debug("Получен PATCH запрос на обновление пользователя пользователя с id: " + userId);
        return userService.updateUser(userId, user);
    }

    @GetMapping
    public List<User> findAllUsers() {
        log.debug("Получен GET запрос на получение списка всех пользователей.");
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User findUser(@PathVariable Long userId) {
        log.debug("Получен GET запрос на получение пользователя с id: {}.", userId);
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void  deleteUser(@PathVariable Long userId) {
        log.debug("Получен DELETE запрос на удаление пользователя с id: {}.", userId);
        userService.deleteUser(userId);
    }
}
