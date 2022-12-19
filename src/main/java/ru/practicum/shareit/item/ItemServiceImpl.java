package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

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
                itemRepository.getReferenceById(itemId)));

        return ItemMapper.toItemDto(itemResponse);
    }

    @Override
    public List<ItemDto> findAllItem(Long ownerId) {

        List<Item> itemList = itemRepository.findAll().stream()
                .filter(e -> e.getOwner().equals(ownerId))
                .collect(Collectors.toList());

        return setLastAndNextBookingForListOfItems(itemList, ownerId);
    }

    @Override
    public ItemDto getUserItem(Long ownerId, Long itemId) {
        Item item = itemRepository.getReferenceById(itemId);

        if (item.getOwner().equals(ownerId)) {
            return setLastAndNextBooking(item);
        } else return ItemMapper.toItemDto(item);
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
        if (!(itemRepository.getReferenceById(itemId).getOwner().equals(ownerId))) {
            throw new EntityNotFoundException("У пользователя с id " + ownerId + " нет вещи с id " + itemId);
        }
    }

    private Booking getLastBooking(List<Booking> itemsBooking) {
        Booking bookingResponse = itemsBooking.get(0);
        for (Booking booking : itemsBooking) {
            if (booking.getEnd().isBefore(bookingResponse.getEnd())) {
                bookingResponse = booking;
            }
        }
        return bookingResponse;
    }

    private Booking getNextBooking(List<Booking> itemsBooking) {
        Booking bookingResponse = itemsBooking.get(0);
        for (Booking booking : itemsBooking) {
            if (booking.getStart().isAfter(bookingResponse.getStart())) {
                bookingResponse = booking;
            }
        }
        return bookingResponse;
    }

    private ItemDto setLastAndNextBooking (Item item) {
        List<Booking> itemsBooking = bookingRepository.searchBookingByItemId(item.getId());
        ItemDto itemDto = ItemMapper.toItemDto(item);

        if (itemsBooking.size() > 0) {

            BookingItemDto lastBooking = BookingMapper.toBookingItemDto(getLastBooking(itemsBooking));
            BookingItemDto nextBooking = BookingMapper.toBookingItemDto(getNextBooking(itemsBooking));

            itemDto.setLastBooking(lastBooking);
            itemDto.setNextBooking(nextBooking);
        }
        return itemDto;
    }

    private List<ItemDto> setLastAndNextBookingForListOfItems (List<Item> items, Long ownerId) {
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (Item item : items) {
            if (item.getOwner().equals(ownerId)) {
                itemDtoList.add(setLastAndNextBooking(item));
            } else itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }
}
