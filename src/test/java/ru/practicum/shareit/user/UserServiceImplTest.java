package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void createUser_whenUserCorrect_thenUserSaved() {
        UserCreateDto userToSave = new UserCreateDto(
                1L,
                "SomeUser",
                "Some.User@mail.com");
        when(userRepository.save(any())).thenReturn(UserMapper.toUser(userToSave));

        UserDto actualUser = userService.createUser(userToSave);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(UserMapper.toUserDto(UserMapper.toUser(userToSave)), actualUser);
        verify(userRepository).save(UserMapper.toUser(userToSave));

        assertEquals(savedUser.getName(), "SomeUser");
        assertEquals(savedUser.getEmail(), "Some.User@mail.com");
    }

    @Test
    void createUser_whenUserNotSave() {
        UserCreateDto userCreateDto = new UserCreateDto(
                1L,
                "SomeUser",
                "Some.User@mail.com");

        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> userService.createUser(userCreateDto));
        verify(userRepository, never()).save(new User());
    }

    @Test
    void updateUser_whenUserCorrect_thenUserUpdate() {
        Long userId = 1L;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("Old");
        oldUser.setEmail("Old@mail.com");

        UserDto newUserDto = new UserDto();
        newUserDto.setName("New");
        oldUser.setEmail("New@mail.com");
        when(userRepository.findById(any())).thenReturn(Optional.of(oldUser));
        when(userRepository.getByEmail(any())).thenReturn(Collections.emptyList());
        when(userRepository.save(any())).thenReturn(oldUser);

        UserDto actualUser = userService.updateUser(userId, newUserDto);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(savedUser.getName(), "New");
        assertEquals(savedUser.getEmail(), "New@mail.com");
    }

    @Test
    void updateUser_whenUserEmailAlreadyExists_thenUserNotUpdate() {
        Long userId = 1L;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setEmail("Old@mail.com");

        UserDto newUserDto = new UserDto();
        newUserDto.setName("New");
        when(userRepository.getByEmail(any())).thenReturn(List.of(oldUser));


        assertThrows(DataIntegrityViolationException.class,
                () -> userService.updateUser(userId, newUserDto));
        verify(userRepository, never()).save(new User());
    }

    @Test
    void updateUser_whenUserNotFound_thenReturnedEntityNotFoundException() {
        long userId = 0L;
        UserDto updateUser = new UserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> userService.updateUser(userId, updateUser));
        assertEquals(entityNotFoundException.getMessage(), "Пользователя с id " + userId + " не существует");
        verify(userRepository, never()).save(new User());
    }

    @Test
    void findAll() {
        Long userId = 1L;
        User saveUser = new User();
        saveUser.setId(userId);
        saveUser.setName("Old");
        saveUser.setEmail("Old@mail.com");
        when(userRepository.findAll()).thenReturn(List.of(saveUser));

        List<UserDto> actualUserList = userService.findAll();

        assertEquals(UserMapper.mapToUserDto(List.of(saveUser)), actualUserList);
    }

    @Test
    void getUser_whenUserFound_thenReturnedUser() {
        long userId = 0L;
        User expectedUser = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        UserDto actualUser = userService.getUser(userId);

        assertEquals(UserMapper.toUserDto(expectedUser), actualUser);
    }

    @Test
    void getUser_whenUserNotFound_thenReturnedEntityNotFoundException() {
        long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> userService.getUser(userId));
        assertEquals(entityNotFoundException.getMessage(), "Пользователя с id " + userId + " не существует");
    }

    @Test
    void deleteUser_whenUserFound_thenDeleteUser() {
        long userId = 0L;
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_whenUserNotFound_thenReturnedEntityNotFoundException() {
        long userId = 0L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> userService.deleteUser(userId));

        assertEquals(entityNotFoundException.getMessage(), "Пользователя с id " + userId + " не существует");
        verify(userRepository, never()).deleteById(userId);
    }
}