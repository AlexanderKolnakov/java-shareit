package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    private static final ObjectMapper om = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController controller;


//    @Test
//    public void shouldCreateItem() throws Exception {
//        Item item = getItem();
//
//        String result = mockMvc.perform(post("/items")
//
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(om.writeValueAsString(item)))
//                .andReturn().getResponse().getContentAsString();
//
//        Item savedItem = om.readValue(result, Item.class);
//        assertNotNull(savedItem);
//        assertNotNull(savedItem.getId());
//        assertEquals(item.getName(), savedItem.getName());
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("usersParam")
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
//    void tryToCreateUserWithBadRequest(User users, String message) throws Exception {
//        String body = om.writeValueAsString(users);
//        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
//                .andExpect(result -> assertTrue(
//                        result.getResolvedException().getMessage().contains(message)));
//    }
//
//    private static Stream<Arguments> usersParam() {
//        return Stream.of(
//                Arguments.of(new User(1L, "o4en_plohoi_email", "name"),
//                        "Введен не корректный email."),
//                Arguments.of(new User(1L, "", "name"),
//                        "Введите email."),
//                Arguments.of(new User(-1L, "norm@email.com", "name"),
//                        "Некорректный номер id."));
//    }
}
