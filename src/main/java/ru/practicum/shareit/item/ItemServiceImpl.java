package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(Long ownerId, Item item) {
        checkOwnerId(ownerId);
        item.setOwner(ownerId);
        Item itemResponse = itemRepository.save(item);
        return ItemMapper.toItemDto(itemResponse);
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, ItemUpdateDto itemUpdateDto) {
        checkOwnerId(ownerId);
        checkItemOwner(ownerId, itemId);
        itemUpdateDto.setOwner(ownerId);
        itemUpdateDto.setId(itemId);

        Item itemResponse = itemRepository.save(ItemMapper.toItem(itemUpdateDto,
                itemRepository.getById(itemId)));

        return ItemMapper.toItemDto(itemResponse);
    }

    @Override
    public List<ItemDto> findAllItem(Long ownerId) {
        List<Item> itemList = itemRepository.findAll().stream().filter(e -> e.getOwner().equals(ownerId))
                .collect(Collectors.toList());
        return ItemMapper.mapToItemDto(itemList);
    }

    @Override
    public ItemDto getUserItem(Long ownerId, Long itemId) {
        Item item = itemRepository.getById(itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemSearchByDescription(String text) {
        return ItemMapper.mapToItemDto(itemRepository.searchItemsByText(text))
                .stream().filter(e -> e.getAvailable().equals(true))
                .collect(Collectors.toList());
    }

    private void checkOwnerId(Long ownerId) {
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new EntityNotFoundException("Пользователя с id " + ownerId + " не существует");
        }
    }

    private void checkItemOwner(Long ownerId, Long itemId) {
        if (!(itemRepository.getById(itemId).getOwner().equals(ownerId))) {
            throw new EntityNotFoundException("У пользователя с id " + ownerId + " нет вещи с id " + itemId);
        }
    }
}
