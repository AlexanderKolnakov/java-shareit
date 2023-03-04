package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
                () -> itemRequestService.createItemRequest(userId, itemRequest));
        assertEquals(entityNotFoundException.getMessage(), "Пользователя с id " + userId + " не существует");
        verify(itemRequestRepository, never()).save(any());
    }

    @Test
    void createItemRequest_whenAllCorrect_thenReturnItemRequestDto() {
        long userId = 1L;
        ItemRequest itemRequest = new ItemRequest();
        User user = new User();

        itemRequest.setId(1L);
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
        long userId = 1L;
        ItemRequest itemRequest = new ItemRequest();
        User user = new User();
        Item item = new Item();

        item.setId(1L);
        item.setName("Some Name");
        item.setDescription("Some Description");
        item.setAvailable(true);
        item.setOwner(1L);
        item.setRequestId(1L);

        itemRequest.setId(1L);
        itemRequest.setDescription("Some Description");
        itemRequest.setCreated(LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.searchItemRequestByUserId(userId)).thenReturn(List.of(itemRequest));
        when(itemRepository.searchItemByRequestId(any())).thenReturn(List.of(item));

        List<ItemRequestDto> actualListItemRequest = itemRequestService.findUserItemRequest(userId);

        assertEquals(actualListItemRequest.size(), 1);
        assertEquals(actualListItemRequest.get(0).getDescription(), itemRequest.getDescription());
        assertEquals(actualListItemRequest.get(0).getItems().get(0).getName(), item.getName());
    }

    @Test
    void findAllItemRequest_whenPageableNotCorrect_thenReturnBabRequest() {
        long userId = 1L;
        int from = -20;
        int size = -20;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        DataIntegrityViolationException dataIntegrityViolationException = assertThrows(DataIntegrityViolationException.class,
                () -> itemRequestService.findAllItemRequest(userId, from, size));
        assertEquals(dataIntegrityViolationException.getMessage(), "Не правильно указаны индексы искомых запросов: "
                + from + " и " + size);
        verify(itemRequestRepository, never()).findAll();
    }

    @Test
    void findAllItemRequest_whenAllCorrect_thenReturnListOfItemRequestDto() {
        long userId = 1L;
        int from = 0;
        int size = 20;
        User user = new User();
        ItemRequest itemRequestOne = new ItemRequest();
        ItemRequest itemRequestTwo = new ItemRequest();

        itemRequestOne.setId(1L);
        itemRequestOne.setDescription("Some Description One");
        itemRequestOne.setCreated(LocalDateTime.now());
        itemRequestOne.setUserRequest(userId);

        itemRequestTwo.setId(2L);
        itemRequestTwo.setDescription("Some Description Two");
        itemRequestTwo.setCreated(LocalDateTime.now());
        itemRequestTwo.setUserRequest(2L);

        Pageable pageable = PageRequest.of(from, size, Sort.by("created").descending());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(itemRequestOne, itemRequestTwo), pageable, 100));

        List<ItemRequestDto> actualListItemRequest = itemRequestService.findAllItemRequest(userId, from, size);

        assertEquals(actualListItemRequest.size(), 1);
        assertEquals(actualListItemRequest.get(0).getDescription(), itemRequestTwo.getDescription());
    }

    @Test
    void findItemRequest_whenItemRequestDosNotExist_thenReturnBadRequest() {
        long requestId = 1L;
        long userId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.searchItemByRequestId(requestId)).thenReturn(Collections.emptyList());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> itemRequestService.findItemRequest(userId, requestId));
        assertEquals(entityNotFoundException.getMessage(), "Запроса с id " + requestId + " не существует");
        verify(itemRequestRepository, never()).findById(requestId);
    }

    @Test
    void findItemRequest_whenAllCorrect_thenReturnItemRequestDto() {
        long requestId = 1L;
        long userId = 1L;
        User user = new User();
        Item item = new Item();
        ItemRequest itemRequestOne = new ItemRequest();

        itemRequestOne.setId(1L);
        itemRequestOne.setDescription("Some Description One");
        itemRequestOne.setCreated(LocalDateTime.now());

        item.setId(1L);
        item.setName("Some Name");
        item.setDescription("Some Description");
        item.setAvailable(true);
        item.setOwner(1L);
        item.setRequestId(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.searchItemByRequestId(requestId)).thenReturn(List.of(item));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequestOne));
        when(itemRepository.searchItemByRequestId(itemRequestOne.getId())).thenReturn(List.of(item));

        ItemRequestDto actualItemRequest = itemRequestService.findItemRequest(userId, requestId);

        assertEquals(actualItemRequest.getDescription(), itemRequestOne.getDescription());
        assertEquals(actualItemRequest.getItems().get(0).getName(), item.getName());
    }
}