package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping()
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateDto user) {
        log.debug("Получен POST запрос на создание пользователя");
        return userClient.createUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody @Valid UserDto userDto) {
        log.debug("Получен PATCH запрос на обновление пользователя пользователя с id: " + userId);
        return userClient.updateUser(userId, userDto);
    }

    @GetMapping
//    public List<UserDto> findAllUsers() {
    public ResponseEntity<Object> findAllUsers() {
        log.debug("Получен GET запрос на получение списка всех пользователей.");
        return userClient.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findUser(@PathVariable Long userId) {
        log.debug("Получен GET запрос на получение пользователя с id: {}.", userId);
        return userClient.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.debug("Получен DELETE запрос на удаление пользователя с id: {}.", userId);
        userClient.deleteUser(userId);
    }
}
