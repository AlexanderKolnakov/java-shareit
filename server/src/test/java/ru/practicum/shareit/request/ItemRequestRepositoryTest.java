package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    ItemRequest itemRequestOne;
    ItemRequest itemRequestTwo;

    @BeforeEach
    private void addItemRequest() {
        itemRequestOne = new ItemRequest(1L, "One", 1L, LocalDateTime.now());
        itemRequestTwo = new ItemRequest(2L, "Two", 2L, LocalDateTime.now());

        itemRequestRepository.save(itemRequestOne);
        itemRequestRepository.save(itemRequestTwo);
    }

    @AfterEach
    private void deleteItemRequest() {
        itemRequestRepository.deleteAll();
    }

    @Test
    void searchItemRequestByUserId() {
        final List<ItemRequest> result = itemRequestRepository
                .searchItemRequestByUserId(itemRequestTwo.getUserRequest());

        assertEquals(1, result.size());
        assertEquals("Two", result.get(0).getDescription());
    }
}