//package ru.practicum.shareit.item;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import ru.practicum.shareit.booking.BookingRepository;
//import ru.practicum.shareit.booking.model.Booking;
//import ru.practicum.shareit.exceptions.BookingException;
//import ru.practicum.shareit.item.dto.CommentDto;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.item.model.Comment;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.user.UserRepository;
//import ru.practicum.shareit.user.model.User;
//
//import javax.persistence.EntityNotFoundException;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ItemServiceImplTest {
//
//    @InjectMocks
//    private ItemServiceImpl itemService;
//
//    @Mock
//    private ItemRepository itemRepository;
//
//    @Mock
//    private CommentRepository commentRepository;
//
//    @Mock
//    private BookingRepository bookingRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Captor
//    private ArgumentCaptor<Item> userArgumentCaptor;
//
//    @Test
//    void createItem_whenItemCorrectAndHaveRequestId_thenItemSaved() {
//        long ownerId = 1L;
//        User user = new User();
//
//        Item saveItem = new Item(
//                1L,
//                "Some Name",
//                "Some Description",
//                true,
//                1L,
//                1L);
//
//        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));
//        when(itemRepository.save(saveItem)).thenReturn(saveItem);
//
//        ItemDto actualItem = itemService.createItem(ownerId, saveItem);
//
//        verify(itemRepository).save(userArgumentCaptor.capture());
//        Item savedItem = userArgumentCaptor.getValue();
//
//        assertEquals(ItemMapper.toItemDtoWithRequest(saveItem), actualItem);
//
//        assertEquals(savedItem.getName(), saveItem.getName());
//        assertEquals(savedItem.getDescription(), saveItem.getDescription());
//    }
//
//    @Test
//    void createItem_whenItemCorrectAndDidntHaveRequestId_thenItemSaved() {
//        long ownerId = 1L;
//        User user = new User();
//
//        Item saveItem = new Item();
//        saveItem.setId(1L);
//        saveItem.setName("Some Name");
//        saveItem.setDescription("Some Description");
//        saveItem.setAvailable(true);
//        saveItem.setOwner(1L);
//
//        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));
//        when(itemRepository.save(saveItem)).thenReturn(saveItem);
//
//        ItemDto actualItem = itemService.createItem(ownerId, saveItem);
//
//        verify(itemRepository).save(userArgumentCaptor.capture());
//        Item savedItem = userArgumentCaptor.getValue();
//
//        assertEquals(ItemMapper.toItemDto(saveItem), actualItem);
//
//        assertEquals(savedItem.getName(), saveItem.getName());
//        assertEquals(savedItem.getDescription(), saveItem.getDescription());
//    }
//
//    @Test
//    void createItem_whenOwnerDoesNotExist_thenItemNotSaved() {
//        long ownerId = 1L;
//        Item saveItem = new Item();
//        saveItem.setId(1L);
//        saveItem.setName("Some Name");
//        saveItem.setDescription("Some Description");
//        saveItem.setAvailable(true);
//        saveItem.setOwner(1L);
//
//        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());
//
//        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
//                () -> itemService.createItem(ownerId, saveItem));
//        assertEquals(entityNotFoundException.getMessage(), "Пользователя с id " + ownerId + " не существует");
//        verify(itemRepository, never()).save(saveItem);
//    }
//
//    @Test
//    void updateItem_whenUpdateItemCorrectAndAllNewParameter_thenItemUpdate() {
//        long ownerId = 1L;
//        long itemId = 1L;
//        User user = new User();
//
//        Item oldItem = new Item();
//        oldItem.setName("Old Name");
//        oldItem.setDescription("Old Description");
//        oldItem.setAvailable(false);
//        oldItem.setOwner(ownerId);
//        oldItem.setId(itemId);
//
//        ItemDto newItem = new ItemDto();
//        newItem.setName("New Name");
//        newItem.setDescription("New Description");
//        newItem.setAvailable(true);
//
//        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));
//        when(itemRepository.getReferenceById(itemId)).thenReturn(oldItem);
//        when(itemRepository.save(any())).thenReturn(oldItem);
//
//        ItemDto actualItem = itemService.updateItem(ownerId, itemId, newItem);
//
//        verify(itemRepository).save(userArgumentCaptor.capture());
//        Item savedItem = userArgumentCaptor.getValue();
//
//        assertEquals(savedItem.getName(), "New Name");
//        assertEquals(savedItem.getDescription(), "New Description");
//        assertEquals(savedItem.getAvailable(), true);
//    }
//
//    @Test
//    void updateItem_whenUpdateItemCorrectAndDidntHaveAnyNewParameter_thenItemUpdate() {
//        long ownerId = 1L;
//        long itemId = 1L;
//        User user = new User();
//
//        Item oldItem = new Item();
//        oldItem.setName("Old Name");
//        oldItem.setDescription("Old Description");
//        oldItem.setAvailable(false);
//        oldItem.setOwner(ownerId);
//        oldItem.setId(itemId);
//
//        ItemDto newItem = new ItemDto();
//
//        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));
//        when(itemRepository.getReferenceById(itemId)).thenReturn(oldItem);
//        when(itemRepository.save(any())).thenReturn(oldItem);
//
//        ItemDto actualItem = itemService.updateItem(ownerId, itemId, newItem);
//
//        verify(itemRepository).save(userArgumentCaptor.capture());
//        Item savedItem = userArgumentCaptor.getValue();
//
//        assertEquals(savedItem.getName(), "Old Name");
//        assertEquals(savedItem.getDescription(), "Old Description");
//        assertEquals(savedItem.getAvailable(), false);
//    }
//
//
//    @Test
//    void updateItem_whenUserDidntHaveThisItem_thenItemNotUpdate() {
//        long userId = 1L;
//        long itemId = 1L;
//        User user = new User();
//        Item oldItem = new Item();
//        oldItem.setOwner(2L);
//
//        ItemDto newItem = new ItemDto();
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(itemRepository.getReferenceById(itemId)).thenReturn(oldItem);
//
//        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
//                () -> itemService.updateItem(userId, itemId, newItem));
//        assertEquals(entityNotFoundException.getMessage(), "У пользователя с id " + userId + " нет вещи с id " + itemId);
//        verify(itemRepository, never()).save(any());
//    }
//
//    @Test
//    void findAllItem_whenPageableNotCorrect_thenReturnBabRequest() {
//        long userId = 1L;
//        int from = -20;
//        int size = -20;
//
//        DataIntegrityViolationException dataIntegrityViolationException = assertThrows(DataIntegrityViolationException.class,
//                () -> itemService.findAllItem(userId, from, size));
//        assertEquals(dataIntegrityViolationException.getMessage(), "Не правильно указаны индексы искомых запросов: "
//                + from + " и " + size);
//        verify(itemRepository, never()).findAll();
//    }
//
//    @Test
//    void findAllItem_whenPageableCorrect_thenReturnListOfItemDto() {
//        long userId = 1L;
//        int from = 0;
//        int size = 20;
//
//        Item itemOne = new Item();
//        Item itemTwo = new Item();
//        itemOne.setOwner(userId);
//        itemOne.setId(2L);
//        itemTwo.setOwner(userId);
//        itemTwo.setId(1L);
//
//        Pageable pageable = PageRequest.of(from, size);
//        when(itemRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(itemOne, itemTwo), pageable, 100));
//
//        List<ItemDto> actualItemList = itemService.findAllItem(userId, from, size);
//
//        assertEquals(actualItemList.get(0).getId(), 1L);
//        assertEquals(actualItemList.get(1).getId(), 2L);
//    }
//
//    @Test
//    void getUserItem_whenUserIsItemOwner_thenReturnItem() {
//        Item itemResponse = new Item();
//        Comment commentOne = new Comment();
//        Booking bookingOne = new Booking();
//        Booking bookingTwo = new Booking();
//        User author = new User();
//
//        long itemId = 1L;
//        long ownerId = 1L;
//
//        author.setName("Author Name");
//        author.setId(1L);
//
//        itemResponse.setId(1L);
//        itemResponse.setName("Item One");
//        itemResponse.setOwner(ownerId);
//
//        commentOne.setText("Comment One");
//        commentOne.setAuthor(author);
//        commentOne.setId(1L);
//        commentOne.setCreated(LocalDateTime.now());
//
//        bookingOne.setStart(LocalDateTime.now().plusDays(1));
//        bookingOne.setEnd(LocalDateTime.now().plusDays(2));
//        bookingOne.setId(1L);
//        bookingOne.setBooker(author);
//
//        bookingTwo.setStart(LocalDateTime.now().plusDays(2));
//        bookingTwo.setEnd(LocalDateTime.now().plusDays(1));
//        bookingTwo.setId(2L);
//        bookingTwo.setBooker(author);
//
//        when(itemRepository.getReferenceById(itemId)).thenReturn(itemResponse);
//        when(bookingRepository.searchBookingByItemId(any())).thenReturn(List.of(bookingOne, bookingTwo));
//        when(commentRepository.searchCommentByItemId(any())).thenReturn(List.of(commentOne));
//
//        ItemDto actualItem = itemService.getUserItem(ownerId, itemId);
//
//        assertEquals(actualItem.getName(), itemResponse.getName());
//        assertEquals(actualItem.getComments().get(0).getText(), commentOne.getText());
//        assertEquals(actualItem.getComments().get(0).getAuthorName(), author.getName());
//        assertEquals(actualItem.getLastBooking().getId(), bookingTwo.getId());
//        assertEquals(actualItem.getNextBooking().getId(), bookingTwo.getId());
//    }
//
//    @Test
//    void getUserItem_whenUserNotItemOwner_thenReturnItem() {
//        Item itemResponse = new Item();
//        Comment commentOne = new Comment();
//        long itemId = 1L;
//        long ownerId = 1L;
//
//        User author = new User();
//        author.setName("Author Name");
//
//        itemResponse.setId(1L);
//        itemResponse.setName("Item One");
//        itemResponse.setOwner(2L);
//        commentOne.setText("Comment One");
//        commentOne.setAuthor(author);
//        commentOne.setId(1L);
//        commentOne.setCreated(LocalDateTime.now());
//
//        when(itemRepository.getReferenceById(itemId)).thenReturn(itemResponse);
//        when(commentRepository.searchCommentByItemId(any())).thenReturn(List.of(commentOne));
//
//        ItemDto actualItem = itemService.getUserItem(ownerId, itemId);
//
//        assertEquals(actualItem.getName(), itemResponse.getName());
//        assertEquals(actualItem.getComments().get(0).getText(), commentOne.getText());
//        assertEquals(actualItem.getComments().get(0).getAuthorName(), author.getName());
//    }
//
//
//    @Test
//    void getItemSearchByDescription_whenPageableNotCorrect_thenReturnBabRequest() {
//        String text = "Some text";
//        int from = -20;
//        int size = -20;
//
//        DataIntegrityViolationException dataIntegrityViolationException = assertThrows(DataIntegrityViolationException.class,
//                () -> itemService.getItemSearchByDescription(text, from, size));
//        assertEquals(dataIntegrityViolationException.getMessage(), "Не правильно указаны индексы искомых запросов: "
//                + from + " и " + size);
//        verify(itemRepository, never()).searchItemsByText(any(), any());
//    }
//
//    @Test
//    void getItemSearchByDescription_whenCorrect_thenReturnListOfItemDto() {
//        Item itemResponse = new Item();
//        String text = "Some text";
//        int from = -0;
//        int size = 20;
//        Pageable pageable = PageRequest.of(from, size);
//
//        itemResponse.setId(1L);
//        itemResponse.setName("Some Name");
//        itemResponse.setDescription("Some Description");
//        itemResponse.setAvailable(true);
//        itemResponse.setOwner(1L);
//
//        when(itemRepository.searchItemsByText(text, pageable)).thenReturn(List.of(itemResponse));
//
//        List<ItemDto> actualItemList = itemService.getItemSearchByDescription(text, from, size);
//
//        verify(itemRepository).searchItemsByText("Some text", pageable);
//        assertEquals(actualItemList.size(), 1);
//        assertEquals(actualItemList.get(0).getName(), itemResponse.getName());
//        assertEquals(actualItemList.get(0).getDescription(), itemResponse.getDescription());
//    }
//
//    @Test
//    void createComment() {
//        long itemId = 1L;
//        long authorId = 1L;
//        User user = new User();
//        Item item = new Item();
//        Booking bookingOne = new Booking();
//        Booking bookingTwo = new Booking();
//        Comment comment = new Comment();
//
//        user.setName("Some Name");
//
//        comment.setId(1L);
//        comment.setText("Some Text");
//        comment.setAuthor(user);
//        comment.setCreated(LocalDateTime.now().plusHours(4));
//
//        bookingOne.setStart(LocalDateTime.now().plusHours(2));
//        bookingTwo.setStart(LocalDateTime.now().plusHours(3));
//
//        when(itemRepository.getReferenceById(itemId)).thenReturn(item);
//        when(userRepository.getReferenceById(authorId)).thenReturn(user);
//        when(userRepository.findById(authorId)).thenReturn(Optional.of(user));
//        when(bookingRepository.searchBookingByItemIdAndUserId(itemId, authorId)).thenReturn(List.of(bookingOne, bookingTwo));
//        when(commentRepository.save(any())).thenReturn(comment);
//
//        CommentDto actualComment = itemService.createComment(authorId, itemId, comment);
//
//        assertEquals(actualComment.getAuthorName(), user.getName());
//        assertEquals(actualComment.getText(), comment.getText());
//        verify(commentRepository).save(comment);
//    }
//
//    @Test
//    void createComment_whenUserDidntTateItem_thenReturnBadRequest() {
//        long itemId = 1L;
//        long authorId = 1L;
//        User user = new User();
//        Comment comment = new Comment();
//
//        when(userRepository.findById(authorId)).thenReturn(Optional.of(user));
//        when(bookingRepository.searchBookingByItemIdAndUserId(itemId, authorId)).thenReturn(Collections.emptyList());
//
//        BookingException bookingException = assertThrows(BookingException.class,
//                () -> itemService.createComment(authorId, itemId, comment));
//        assertEquals(bookingException.getMessage(), "Пользователя с id " + authorId + " не брал эту вещ в аренду.");
//        verify(commentRepository, never()).save(any());
//    }
//
//    @Test
//    void createComment_whenUCommentCreatedBeforeStartBooking_thenReturnBadRequest() {
//        long itemId = 1L;
//        long authorId = 1L;
//        User user = new User();
//        Comment comment = new Comment();
//        Booking booking = new Booking();
//
//        comment.setCreated(LocalDateTime.now().plusHours(1));
//        booking.setStart(LocalDateTime.now().plusHours(2));
//
//        when(userRepository.findById(authorId)).thenReturn(Optional.of(user));
//        when(bookingRepository.searchBookingByItemIdAndUserId(itemId, authorId)).thenReturn(List.of(booking));
//
//        BookingException bookingException = assertThrows(BookingException.class,
//                () -> itemService.createComment(authorId, itemId, comment));
//        assertEquals(bookingException.getMessage(), "Нельзя оставлять комментарий, до начала аренды.");
//        verify(commentRepository, never()).save(any());
//    }
//}