package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping()
    public ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestBody ItemRequest itemRequest) {
        itemRequest.setCreated(LocalDateTime.now());
        log.debug("Получен POST запрос на создание запроса вещи с описанием: '" + itemRequest.getDescription() + "'");
        return itemRequestService.createItemRequest(userId, itemRequest);
    }

    @GetMapping
    public List<ItemRequestDto> findAllUsersItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен GET запрос на получение списка всех своих запросов вещей от пользователя с id: " + userId);
        return itemRequestService.findUserItemRequest(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @RequestParam(name = "size", defaultValue = "20") int size) {
        log.debug("Получен GET запрос на получение списка всех чужих запросов вещей от пользователя с id: " + userId);
        return itemRequestService.findAllItemRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long requestId) {
        log.debug("Получен GET запрос на получение данных об одном конкретном запросе от пользователя с id: " + userId);
        return itemRequestService.findItemRequest(userId, requestId);
    }
}
