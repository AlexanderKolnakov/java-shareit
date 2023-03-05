package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;


@RestController
@Slf4j
@AllArgsConstructor
@Validated
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping()
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @RequestBody @Valid Item item) {
        log.debug("Получен POST запрос на создание вещи");
        return itemClient.createItem(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @PathVariable Long itemId,
                                             @RequestBody ItemDto itemUpdateDto) {
        log.debug("Получен PATCH запрос на обновление вещи с id: " + itemId + " у пользователя пользователя с id: " + ownerId);
        return itemClient.updateItem(ownerId, itemId, itemUpdateDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUsersItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                   @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @RequestParam(name = "size", defaultValue = "20") int size) {
        log.debug("Получен GET запрос на получение списка всех вещей пользователя с id: " + ownerId);
        return itemClient.findAllItem(ownerId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findUserItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                               @PathVariable Long itemId) {
        log.debug("Получен GET запрос на получение вещи с id: " + itemId + " у пользователя с id: {}.", ownerId);
        return itemClient.getUserItem(ownerId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemByDescription(
                                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam String text,
                                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @RequestParam(name = "size", defaultValue = "20") int size) {
        log.debug("Получен GET запрос на получение вещи по описанию строки, text =" + text);
        if (text.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        } else {
            return itemClient.getItemSearchByDescription(text, from, size, userId);
        }
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long authorId,
                                                @PathVariable Long itemId,
                                                @RequestBody @Valid Comment comment) {
        comment.setCreated(LocalDateTime.now());
        log.debug("Получен POST запрос на создание комментария");
        return itemClient.createComment(authorId, itemId, comment);
    }
}
