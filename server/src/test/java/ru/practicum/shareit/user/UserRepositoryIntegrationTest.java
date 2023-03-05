package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    User userOne;

    User userTwo;

    @BeforeEach
    private void addUser() {
        userOne = new User(1L, "One", "One@Email.com");
        userTwo = new User(2L, "Two", "Two@Email.com");

        userRepository.save(userOne);
        userRepository.save(userTwo);
    }

    @AfterEach
    private void deleteUser() {
        userRepository.deleteAll();
    }

    @Test
    void getByEmail() {
        final List<User> result = userRepository.getByEmail(userTwo.getEmail());

        assertEquals(1, result.size());
        assertEquals("Two", result.get(0).getName());
    }
}