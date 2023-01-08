package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ItemDto createItem(Long ownerId, Item item) {
        checkOwnerId(ownerId);
        item.setOwner(ownerId);
        Item itemResponse = itemRepository.save(item);
        return ItemMapper.toItemDto(itemResponse);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
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
                .sorted(Comparator.comparing(Item::getId))
                .collect(Collectors.toList());

        return setLastAndNextBookingForListOfItems(itemList, ownerId);
    }

    @Override
    public ItemDto getUserItem(Long ownerId, Long itemId) {
        Item item = itemRepository.getReferenceById(itemId);

        if (item.getOwner().equals(ownerId)) {
            return setComments(setLastAndNextBooking(item));
        } else return setComments(ItemMapper.toItemDto(item));
    }

    @Override
    public List<ItemDto> getItemSearchByDescription(String text) {
        return ItemMapper.mapToItemDto(itemRepository.searchItemsByText(text))
                .stream().filter(e -> e.getAvailable().equals(true))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CommentDto createComment(Long authorId, Long itemId, Comment comment) {
        checkOwnerId(authorId);

        Item item = itemRepository.getReferenceById(itemId);
        User user = userRepository.getReferenceById(authorId);

        List<Booking> itemsBooking = bookingRepository.searchBookingByItemIdAndUserId(itemId, authorId);

        checkUserBookingItem(authorId, itemId);
        checkDataCommentCreate(comment.getCreated(), itemsBooking.get(0).getStart());

        comment.setAuthor(user);
        comment.setItem(item);

        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
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

    private ItemDto setLastAndNextBooking(Item item) {
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

    private ItemDto setComments(ItemDto itemDto) {

//        List<Comment> commentList = commentRepository.searchCommentByItemId(itemDto.getId());
        List<Comment> commentList = new ArrayList<>();
        itemDto.setComments(CommentMapper.mapToCommentDto(commentList));
        return itemDto;
    }

    private List<ItemDto> setLastAndNextBookingForListOfItems(List<Item> items, Long ownerId) {
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (Item item : items) {
            if (item.getOwner().equals(ownerId)) {
                itemDtoList.add(setLastAndNextBooking(item));
            } else itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }

    private void checkDataCommentCreate(LocalDateTime checkCommentData, LocalDateTime dataStart) {
        if (checkCommentData.isBefore(dataStart)) {
            throw new BookingException("checkCommentData - " + checkCommentData +
                    "dataStart - " + dataStart +
                    " Нельзя оставлять комментарий, до начала аренды.");
        }
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

    private void checkUserBookingItem(Long userId, Long itemId) {
        if (!((bookingRepository.searchBookingByItemIdAndUserId(itemId, userId).size()) > 0)) {
            throw new BookingException("Пользователя с id " + userId + " не брал эту вещ в аренду.");
        }
    }
}
