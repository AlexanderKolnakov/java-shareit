package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import javax.validation.Valid;



@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping()
    public Item createUser(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                           @RequestBody @Valid Item item) {
        log.debug("Получен POST запрос на создание вещи");
        return itemService.createItem(ownerId, item);
    }

}
