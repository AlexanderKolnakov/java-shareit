package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.user.model.User;


import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Fixtures.getUser;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private static final ObjectMapper om = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateUser() throws Exception {
        User user = getUser();

        String result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();

        User savedUser = om.readValue(result, User.class);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(user.getName(), savedUser.getName());
    }


    @ParameterizedTest
    @MethodSource("usersParam")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void tryToCreateUserWithBadRequest(User users, String message) throws Exception {
        String body = om.writeValueAsString(users);
        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        Objects.requireNonNull(result.getResolvedException()).getMessage().contains(message)));
    }

    private static Stream<Arguments> usersParam() {
        return Stream.of(
                Arguments.of(new User(1L, "o4en_plohoi_email", "name"),
                        "Введен не корректный email."),
                Arguments.of(new User(1L, "", "name"),
                        "Введите email."),
                Arguments.of(new User(-1L, "norm@email.com", "name"),
                        "Некорректный номер id."));
    }
}
