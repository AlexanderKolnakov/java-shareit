package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

public class ItemRequestMapper {

    public static ItemRequestDto itemRequestToDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

    public static List<ItemRequestDto> mapItemRequestToDto(List<ItemRequest> itemRequestDtoList) {

        List<ItemRequestDto> result = new ArrayList<>();

        for (ItemRequest itemRequestDto : itemRequestDtoList) {
            result.add(itemRequestToDto(itemRequestDto));
        }
        return result;
    }
}
