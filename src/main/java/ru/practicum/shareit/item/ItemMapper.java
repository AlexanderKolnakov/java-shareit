package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner()
        );
    }


    public static List<ItemDto> mapToItemDto(Iterable<Item> items) {
        List<ItemDto> result = new ArrayList<>();

        for (Item item : items) {
            result.add(toItemDto(item));
        }
        return result;
    }

    public static Item toItem(ItemUpdateDto itemUpdateDto, Item itemFromRepository) {
        Item item = new Item();
        item.setId(itemUpdateDto.getId());
        if (itemUpdateDto.getName() != null) {
            item.setName(itemUpdateDto.getName());
        } else {
            item.setName(itemFromRepository.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            item.setDescription(itemUpdateDto.getDescription());
        } else {
            item.setDescription(itemFromRepository.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            item.setAvailable(itemUpdateDto.getAvailable());
        } else {
            item.setAvailable(itemFromRepository.getAvailable());
        }
        if (itemUpdateDto.getOwner() != null) {
            item.setOwner(itemUpdateDto.getOwner());
        } else {
            item.setOwner(itemFromRepository.getOwner());
        }
        return item;
    }
}

