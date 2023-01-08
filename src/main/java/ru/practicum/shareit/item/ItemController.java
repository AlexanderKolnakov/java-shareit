package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping()
    public ItemDto createUser(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @RequestBody @Valid Item item) {
        log.debug("Получен POST запрос на создание вещи");
        return itemService.createItem(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @PathVariable Long itemId,
                              @RequestBody Item item) {
        log.debug("Получен PATCH запрос на обновление вещи с id: " + itemId + " у пользователя пользователя с id: " + ownerId);
        return itemService.updateItem(ownerId, itemId, item);
    }

    @GetMapping
    public List<ItemDto> findAllUsersItem(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.debug("Получен GET запрос на получение списка всех вещей пользователя с id: " + ownerId);
        return itemService.findAllItem(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findUserItem(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long itemId) {
        log.debug("Получен GET запрос на получение вещи с id: " + itemId + " у пользователя с id: {}.", ownerId);
        return itemService.getUserItem(ownerId, itemId);
    }

    @GetMapping("/search")
    List<ItemDto> findItemByDescription(@RequestParam String text) {
        log.debug("Получен GET запрос на получение вещи по описанию строки, text =" + text);
        if (text.isEmpty()) {
            return Collections.emptyList();
        } else {
            return itemService.getItemSearchByDescription(text);
        }
    }
}
