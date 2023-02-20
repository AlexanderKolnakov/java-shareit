package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.text.Collator;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Captor
    private ArgumentCaptor<ItemRequest> userArgumentCaptor;

    @Test
    void createItemRequest_whenUserDoesNotExist_thenReturnBadRequest() {
        long userId = 1L;
        ItemRequest itemRequest = new ItemRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> itemRequestService.createItemRequest(userId,itemRequest));
        assertEquals(entityNotFoundException.getMessage(), "Пользователя с id " + userId + " не существует");
        verify(itemRequestRepository, never()).save(any());
    }

    @Test
    void createItemRequest_whenAllCorrect_thenReturnItemRequestDto() {
        long userId = 1L;
        ItemRequest itemRequest = new ItemRequest();
        User user = new User();

        ItemRequest.setId(1L);
        itemRequest.setDescription("Some Description");
        itemRequest.setCreated(LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);

        ItemRequestDto actualItemRequest = itemRequestService.createItemRequest(userId, itemRequest);

        verify(itemRequestRepository).save(userArgumentCaptor.capture());
        ItemRequestDto savedItemRequest = ItemRequestMapper.itemRequestToDto(userArgumentCaptor.getValue());

        assertEquals(ItemRequestMapper.itemRequestToDto(itemRequest), actualItemRequest);
        assertEquals(savedItemRequest.getDescription(), itemRequest.getDescription());
        verify(itemRequestRepository).save(any());

    }

    @Test
    void findUserItemRequest() {
    }

    @Test
    void findAllItemRequest() {
    }

    @Test
    void findItemRequest() {
    }
}