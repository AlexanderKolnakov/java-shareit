package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    Item itemOne;

    Item itemTwo;

    @BeforeEach
    private void addItems() {
        itemOne = new Item(1L, "Some One Saw", "description by one mechanical axe", true, 1L, 1L);
        itemTwo = new Item(2L, "Two", "description Two", true, 2L, 2L);

        itemRepository.save(itemOne);
        itemRepository.save(itemTwo);
    }

    @AfterEach
    private void deleteAllItem() {
        itemRepository.deleteAll();
    }

    @Test
    void searchItemsByTextInName() {
        final List<Item> result = itemRepository.searchItemsByText("saw");

        assertEquals(1, result.size());
        assertEquals("Some One Saw", result.get(0).getName());
    }

    @Test
    void searchItemsByTextInDescription() {
        final List<Item> result = itemRepository.searchItemsByText("aXe");

        assertEquals(1, result.size());
        assertEquals("Some One Saw", result.get(0).getName());
    }

    @Test
    void searchItemByRequestId() {
        final List<Item> result = itemRepository.searchItemByRequestId(itemTwo.getRequestId());

        assertEquals(1, result.size());
        assertEquals("Two", result.get(0).getName());
    }
}