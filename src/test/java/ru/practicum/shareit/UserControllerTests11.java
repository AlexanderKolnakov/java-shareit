//package ru.practicum.shareit;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.shareit.model.User;
//
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase
//public class UserControllerTests {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
//    void tryToCreateFilmWithAllRequestGood() throws Exception {
//        User user = getUser();
//
//        mockMvc.perform(post("/users").
//                        content(objectMapper.writeValueAsString(getUser())).
//                        contentType(MediaType.APPLICATION_JSON)).
//                andExpect(status().is2xxSuccessful()).
//                andExpect(jsonPath("$.name").value("Test NAME")).
//                andExpect(jsonPath("$.id").value(1));
//    }
//
//    public static User getUser() {
//        User user = new User();
//        user.setName("Test NAME");
//        user.setEmail("test@email.ru");
//        user.setId(1L);
//        return user;
//    }
//}
