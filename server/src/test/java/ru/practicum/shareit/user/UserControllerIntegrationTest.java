package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @SneakyThrows
    void createUser_whenUserValid_thenReturnUser() {
        UserCreateDto userCreate = new UserCreateDto();
        userCreate.setEmail("Some@email.com");
        when(userService.createUser(userCreate))
                .thenReturn(UserMapper.toUserDto(UserMapper.toUser(userCreate)));

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userCreate), result);
    }

//    @Test
//    @SneakyThrows
//    void createUser_whenUserNotValid_thenReturnBadRequest() {
//        UserCreateDto userCreate = new UserCreateDto();
//        userCreate.setEmail("notEmail");
//
//        String result = mockMvc.perform(post("/users")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(userCreate)))
//                .andExpect(status().isBadRequest())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertEquals("Введен не корректный email.", result);
//        verify(userService, never()).createUser(userCreate);
//    }

    @Test
    @SneakyThrows
    void updateUser_whenUserValid_thenReturnUser() {
        long userId = 1L;
        UserDto userCreate = new UserDto();
        userCreate.setEmail("correctEmail@email.com");
        userCreate.setName("SomeName");
        when(userService.updateUser(userId, userCreate))
                .thenReturn(userCreate);

        String result = mockMvc.perform(patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userCreate), result);
    }

//    @Test
//    @SneakyThrows
//    void updateUser_whenUserNotValid_thenReturnBadRequest() {
//        long userId = 1L;
//        UserDto userCreate = new UserDto();
//        userCreate.setEmail("NotCorrectEmail");
//        userCreate.setName("SomeName");
//        when(userService.updateUser(userId, userCreate))
//                .thenReturn(userCreate);
//
//        String result = mockMvc.perform(patch("/users/{id}", userId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(userCreate)))
//                .andExpect(status().isBadRequest())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertEquals("Введен не корректный email.", result);
//    }

    @Test
    @SneakyThrows
    void findUser() {
        long userId = 0L;
        mockMvc.perform(get("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService).getUser(userId);
    }

    @Test
    @SneakyThrows
    void findAllUsers() {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService).findAll();
    }

    @Test
    @SneakyThrows
    void deleteUser() {
        long userId = 0L;
        mockMvc.perform(delete("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService).deleteUser(userId);
    }
}