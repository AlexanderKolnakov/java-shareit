package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(Long ownerId, ItemRequest itemRequest);

    List<ItemRequestDto> findUserItemRequest(Long userId);

    List<ItemRequestDto> findAllItemRequest(Long userId, int from, int size);

    ItemRequestDto findItemRequest(Long userId, Long requestId);
}
