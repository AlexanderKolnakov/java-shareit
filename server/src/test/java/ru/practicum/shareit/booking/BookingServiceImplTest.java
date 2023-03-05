package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<Booking> userArgumentCaptor;

    @Test
    void createBooking_whenUserDoseNotExist_thenReturnBadRequest() {
        long bookerId = 1L;
        BookingRequestDto bookingRequestDto = new BookingRequestDto();

        when(userRepository.findById(bookerId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> bookingService.createBooking(bookerId, bookingRequestDto));
        assertEquals(entityNotFoundException.getMessage(), "Пользователя с id " + bookerId + " не существует");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_whenDateEndBookingRequestIsBeforeStart_thenReturnBadRequest() {
        long bookerId = 1L;
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        User user = new User();

        bookingRequestDto.setEnd(LocalDateTime.now().plusHours(1));
        bookingRequestDto.setStart(LocalDateTime.now().plusHours(2));

        when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));

        BookingException bookingException = assertThrows(BookingException.class,
                () -> bookingService.createBooking(bookerId, bookingRequestDto));
        assertEquals(bookingException.getMessage(), "Дата конца бронирования не может быть раньше чем ее начало.");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_whenItemDoseNotExist_thenReturnBadRequest() {
        long bookerId = 1L;
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        User user = new User();
        Item item = new Item();

        bookingRequestDto.setEnd(LocalDateTime.now().plusHours(2));
        bookingRequestDto.setStart(LocalDateTime.now().plusHours(1));
        bookingRequestDto.setItemId(1L);

        when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
        when(itemRepository.getReferenceById(bookingRequestDto.getItemId())).thenReturn(item);

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> bookingService.createBooking(bookerId, bookingRequestDto));
        assertEquals(entityNotFoundException.getMessage(), "Вещи с id " + item.getId() + " не существует");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_whenItemAvailableIsFalse_thenReturnBadRequest() {
        long bookerId = 1L;
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        User user = new User();
        Item item = new Item();

        bookingRequestDto.setEnd(LocalDateTime.now().plusHours(2));
        bookingRequestDto.setStart(LocalDateTime.now().plusHours(1));
        bookingRequestDto.setItemId(1L);

        item.setId(1L);
        item.setAvailable(false);

        when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
        when(itemRepository.getReferenceById(bookingRequestDto.getItemId())).thenReturn(item);

        BookingException bookingException = assertThrows(BookingException.class,
                () -> bookingService.createBooking(bookerId, bookingRequestDto));
        assertEquals(bookingException
                .getMessage(), "Вещь с id " + item.getId() + " сейчас недоступна для бронирования.");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_whenBookerIsItemOwner_thenReturnBadRequest() {
        long bookerId = 1L;
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        User user = new User();
        Item item = new Item();

        bookingRequestDto.setEnd(LocalDateTime.now().plusHours(2));
        bookingRequestDto.setStart(LocalDateTime.now().plusHours(1));
        bookingRequestDto.setItemId(1L);

        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(bookerId);

        when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
        when(itemRepository.getReferenceById(bookingRequestDto.getItemId())).thenReturn(item);

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> bookingService.createBooking(bookerId, bookingRequestDto));
        assertEquals(entityNotFoundException.getMessage(), "Владелец вещи не может взять ее же в аренду.");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_whenAllCorrect_thenReturnBookingDto() {
        long bookerId = 1L;
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        User user = new User();
        Item item = new Item();

        bookingRequestDto.setEnd(LocalDateTime.now().plusHours(2));
        bookingRequestDto.setStart(LocalDateTime.now().plusHours(1));
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStatus(Status.WAITING);

        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(2L);

        user.setName("Some Name");

        when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
        when(itemRepository.getReferenceById(bookingRequestDto.getItemId())).thenReturn(item);
        when(userRepository.getReferenceById(bookerId)).thenReturn(user);
        when(bookingRepository.save(any())).thenReturn(BookingMapper.toBookingFromBookingRequestDto(bookingRequestDto, item, user));

        BookingDto actualBooking = bookingService.createBooking(bookerId, bookingRequestDto);

        verify(bookingRepository).save(userArgumentCaptor.capture());
        BookingDto savedBooking = BookingMapper.toBookingDto(userArgumentCaptor.getValue());

        assertEquals(savedBooking, actualBooking);
        assertEquals(savedBooking.getItem().getOwner(), item.getOwner());
        assertEquals(savedBooking.getBooker().getName(), user.getName());
        verify(bookingRepository).save(any());
    }

    @Test
    void changeBookingStatus_whenUserDoseNotItemOwner_thenReturnBadRequest() {
        long bookingId = 1L;
        long userId = 2L;
        Boolean approved = true;
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();

        user.setId(1L);
        booking.setItem(item);
        item.setOwner(user.getId());

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> bookingService.changeBookingStatus(userId, bookingId, approved));
        assertEquals(entityNotFoundException.getMessage(), "Пользователь с id " + userId +
                " не может подтвердить бронирование этой вещи, " +
                "так как не является ее владельцем");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void changeBookingStatus_whenBookingStatusAlreadyApproved_thenReturnBadRequest() {
        long bookingId = 1L;
        long userId = 1L;
        Boolean approved = true;
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();

        user.setId(1L);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);
        item.setOwner(user.getId());

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.getReferenceById(bookingId)).thenReturn(booking);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        BookingException bookingException = assertThrows(BookingException.class,
                () -> bookingService.changeBookingStatus(userId, bookingId, approved));
        assertEquals(bookingException.getMessage(), "Статус бронирования с id " + bookingId +
                " уже подтвержден.");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void changeBookingStatus_whenOwnerApprovedBooking_thenReturnBookingDto() {
        long bookingId = 1L;
        long userId = 1L;
        Boolean approved = true;
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();

        user.setId(1L);
        user.setName("Some Name");
        user.setEmail("Some@email.com");
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        booking.setBooker(user);
        item.setOwner(user.getId());

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.getReferenceById(bookingId)).thenReturn(booking);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDto actualBooking = bookingService.changeBookingStatus(userId, bookingId, approved);
        verify(bookingRepository).save(userArgumentCaptor.capture());
        BookingDto savedBooking = BookingMapper.toBookingDto(userArgumentCaptor.getValue());

        assertEquals(savedBooking, actualBooking);
        assertEquals(savedBooking.getItem().getOwner(), item.getOwner());
        assertEquals(savedBooking.getBooker().getName(), user.getName());
        assertEquals(savedBooking.getStatus(), Status.APPROVED);
        verify(bookingRepository).save(any());
    }

    @Test
    void changeBookingStatus_whenUserOwnerNotApprovedBooking_thenReturnBookingDto() {
        long bookingId = 1L;
        long userId = 1L;
        Boolean approved = false;
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();

        user.setId(1L);
        user.setName("Some Name");
        user.setEmail("Some@email.com");
        booking.setItem(item);
        booking.setBooker(user);
        item.setOwner(user.getId());

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.getReferenceById(bookingId)).thenReturn(booking);
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDto actualBooking = bookingService.changeBookingStatus(userId, bookingId, approved);
        verify(bookingRepository).save(userArgumentCaptor.capture());
        BookingDto savedBooking = BookingMapper.toBookingDto(userArgumentCaptor.getValue());

        assertEquals(savedBooking, actualBooking);
        assertEquals(savedBooking.getItem().getOwner(), item.getOwner());
        assertEquals(savedBooking.getBooker().getName(), user.getName());
        assertEquals(savedBooking.getStatus(), Status.REJECTED);
        verify(bookingRepository).save(any());
    }

    @Test
    void getBooking_whenBookingDoseNotExist_thenReturnBadRequest() {
        long bookingId = 1L;
        long userId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> bookingService.getBooking(userId, bookingId));
        assertEquals(entityNotFoundException.getMessage(), "Бронирования с id " + bookingId + " не существует");
        verify(bookingRepository, never()).getById(any());
    }

    @Test
    void getBooking_whenUserDoseNotItemOwnerOrBooker_thenReturnBadRequest() {
        long bookingId = 1L;
        long userId = 1L;
        User user = new User();
        Item item = new Item();
        Booking booking = new Booking();

        booking.setItem(item);
        booking.setBooker(user);
        item.setOwner(2L);
        user.setId(2L);
        user.setId(2L);
        user.setName("Some Name");
        user.setEmail("Some@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> bookingService.getBooking(userId, bookingId));
        assertEquals(entityNotFoundException.getMessage(), ("Пользователь с id " + userId +
                " не может получить информацию " +
                "о бронировании с id  " + bookingId + " так как не является ее владельцем или арендатором"));
    }

    @Test
    void getBooking_whenAllCorrect_thenReturnBookingDto() {
        long bookingId = 1L;
        long userId = 1L;
        User user = new User();
        Item item = new Item();
        Booking booking = new Booking();

        booking.setItem(item);
        booking.setBooker(user);
        item.setOwner(1L);
        user.setId(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        BookingDto actualBooking = bookingService.getBooking(userId, bookingId);

        assertEquals(actualBooking.getItem().getOwner(), item.getOwner());
        assertEquals(actualBooking.getBooker().getId(), user.getId());
    }

    @Test
    void getAllBooking_whenStateALLAndIsOwner_thenReturnListOfBooking() {
        long userId = 1L;
        String state = "ALL";
        Boolean isOwner = true;
        int from = 0;
        int size = 20;
        User user = new User();
        Booking booking = new Booking();
        Item item = new Item();

        booking.setBooker(user);
        booking.setItem(item);
        item.setName("Some Name");
        user.setEmail("Some@email.com");

        Pageable pageable = PageRequest.of(from, size, Sort.by("start").descending());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOwner(userId, pageable)).thenReturn(List.of(booking));

        List<BookingDto> actualBookingList = bookingService.getAllBooking(userId, state, isOwner, from, size);

        assertEquals(actualBookingList.size(), 1);
        assertEquals(actualBookingList.get(0).getItem().getName(), item.getName());
        assertEquals(actualBookingList.get(0).getBooker().getEmail(), user.getEmail());
    }

    @Test
    void getAllBooking_whenStateALLAndNotOwner_thenReturnListOfBooking() {
        long userId = 1L;
        String state = "ALL";
        Boolean isOwner = false;
        int from = 0;
        int size = 20;
        User user = new User();
        Booking booking = new Booking();
        Item item = new Item();

        booking.setBooker(user);
        booking.setItem(item);
        item.setName("Some Name");
        user.setEmail("Some@email.com");

        Pageable pageable = PageRequest.of(from, size, Sort.by("start").descending());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOtherUser(userId, pageable)).thenReturn(List.of(booking));

        List<BookingDto> actualBookingList = bookingService.getAllBooking(userId, state, isOwner, from, size);

        assertEquals(actualBookingList.size(), 1);
        assertEquals(actualBookingList.get(0).getItem().getName(), item.getName());
        assertEquals(actualBookingList.get(0).getBooker().getEmail(), user.getEmail());
    }

    @Test
    void getAllBooking_whenStateFUTUREAndIsOwner_thenReturnListOfBooking() {
        long userId = 1L;
        String state = "FUTURE";
        Boolean isOwner = true;
        int from = 0;
        int size = 20;
        User user = new User();
        Booking booking = new Booking();
        Item item = new Item();

        booking.setBooker(user);
        booking.setItem(item);
        item.setName("Some Name");
        user.setEmail("Some@email.com");

        Pageable pageable = PageRequest.of(from, size, Sort.by("start").descending());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findFutureByOwner(userId, pageable)).thenReturn(List.of(booking));

        List<BookingDto> actualBookingList = bookingService.getAllBooking(userId, state, isOwner, from, size);

        assertEquals(actualBookingList.size(), 1);
        assertEquals(actualBookingList.get(0).getItem().getName(), item.getName());
        assertEquals(actualBookingList.get(0).getBooker().getEmail(), user.getEmail());
    }

    @Test
    void getAllBooking_whenStateFUTUREAndNotOwner_thenReturnListOfBooking() {
        long userId = 1L;
        String state = "FUTURE";
        Boolean isOwner = false;
        int from = 0;
        int size = 20;
        User user = new User();
        Booking booking = new Booking();
        Item item = new Item();

        booking.setBooker(user);
        booking.setItem(item);
        item.setName("Some Name");
        user.setEmail("Some@email.com");

        Pageable pageable = PageRequest.of(from, size, Sort.by("start").descending());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findFutureByOtherUser(userId, pageable)).thenReturn(List.of(booking));

        List<BookingDto> actualBookingList = bookingService.getAllBooking(userId, state, isOwner, from, size);

        assertEquals(actualBookingList.size(), 1);
        assertEquals(actualBookingList.get(0).getItem().getName(), item.getName());
        assertEquals(actualBookingList.get(0).getBooker().getEmail(), user.getEmail());
    }

    @Test
    void getAllBooking_whenStateWAITINGAndIsOwner_thenReturnListOfBooking() {
        long userId = 1L;
        String state = "WAITING";
        Boolean isOwner = true;
        int from = 0;
        int size = 20;
        User user = new User();
        Booking booking = new Booking();
        Item item = new Item();

        booking.setBooker(user);
        booking.setItem(item);
        item.setName("Some Name");
        user.setEmail("Some@email.com");

        Pageable pageable = PageRequest.of(from, size, Sort.by("start").descending());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByStatusAndByOwner(userId, Status.WAITING, pageable)).thenReturn(List.of(booking));

        List<BookingDto> actualBookingList = bookingService.getAllBooking(userId, state, isOwner, from, size);

        assertEquals(actualBookingList.size(), 1);
        assertEquals(actualBookingList.get(0).getItem().getName(), item.getName());
        assertEquals(actualBookingList.get(0).getBooker().getEmail(), user.getEmail());
    }

    @Test
    void getAllBooking_whenStateWAITINGAndNotOwner_thenReturnListOfBooking() {
        long userId = 1L;
        String state = "WAITING";
        Boolean isOwner = false;
        int from = 0;
        int size = 20;
        User user = new User();
        Booking booking = new Booking();
        Item item = new Item();

        booking.setBooker(user);
        booking.setItem(item);
        item.setName("Some Name");
        user.setEmail("Some@email.com");

        Pageable pageable = PageRequest.of(from, size, Sort.by("start").descending());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByStatusAndByOtherUser(userId, Status.WAITING, pageable)).thenReturn(List.of(booking));

        List<BookingDto> actualBookingList = bookingService.getAllBooking(userId, state, isOwner, from, size);

        assertEquals(actualBookingList.size(), 1);
        assertEquals(actualBookingList.get(0).getItem().getName(), item.getName());
        assertEquals(actualBookingList.get(0).getBooker().getEmail(), user.getEmail());
    }

    @Test
    void getAllBooking_whenStateREJECTEDAndIsOwner_thenReturnListOfBooking() {
        long userId = 1L;
        String state = "REJECTED";
        Boolean isOwner = true;
        int from = 0;
        int size = 20;
        User user = new User();
        Booking booking = new Booking();
        Item item = new Item();

        booking.setBooker(user);
        booking.setItem(item);
        item.setName("Some Name");
        user.setEmail("Some@email.com");

        Pageable pageable = PageRequest.of(from, size, Sort.by("start").descending());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByStatusAndByOwner(userId, Status.REJECTED, pageable)).thenReturn(List.of(booking));

        List<BookingDto> actualBookingList = bookingService.getAllBooking(userId, state, isOwner, from, size);

        assertEquals(actualBookingList.size(), 1);
        assertEquals(actualBookingList.get(0).getItem().getName(), item.getName());
        assertEquals(actualBookingList.get(0).getBooker().getEmail(), user.getEmail());
    }

    @Test
    void getAllBooking_whenStateREJECTEDAndNotOwner_thenReturnListOfBooking() {
        long userId = 1L;
        String state = "REJECTED";
        Boolean isOwner = false;
        int from = 0;
        int size = 20;
        User user = new User();
        Booking booking = new Booking();
        Item item = new Item();

        booking.setBooker(user);
        booking.setItem(item);
        item.setName("Some Name");
        user.setEmail("Some@email.com");

        Pageable pageable = PageRequest.of(from, size, Sort.by("start").descending());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByStatusAndByOtherUser(userId, Status.REJECTED, pageable)).thenReturn(List.of(booking));

        List<BookingDto> actualBookingList = bookingService.getAllBooking(userId, state, isOwner, from, size);

        assertEquals(actualBookingList.size(), 1);
        assertEquals(actualBookingList.get(0).getItem().getName(), item.getName());
        assertEquals(actualBookingList.get(0).getBooker().getEmail(), user.getEmail());
    }

    @Test
    void getAllBooking_whenStatePASTAndIsOwner_thenReturnListOfBooking() {
        long userId = 1L;
        String state = "PAST";
        Boolean isOwner = true;
        int from = 0;
        int size = 20;
        User user = new User();
        Booking booking = new Booking();
        Item item = new Item();

        booking.setBooker(user);
        booking.setItem(item);
        item.setName("Some Name");
        user.setEmail("Some@email.com");

        Pageable pageable = PageRequest.of(from, size, Sort.by("start").descending());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findPastByOwner(userId, pageable)).thenReturn(List.of(booking));

        List<BookingDto> actualBookingList = bookingService.getAllBooking(userId, state, isOwner, from, size);

        assertEquals(actualBookingList.size(), 1);
        assertEquals(actualBookingList.get(0).getItem().getName(), item.getName());
        assertEquals(actualBookingList.get(0).getBooker().getEmail(), user.getEmail());
    }

    @Test
    void getAllBooking_whenStatePASTAndNotOwner_thenReturnListOfBooking() {
        long userId = 1L;
        String state = "PAST";
        Boolean isOwner = false;
        int from = 0;
        int size = 20;
        User user = new User();
        Booking booking = new Booking();
        Item item = new Item();

        booking.setBooker(user);
        booking.setItem(item);
        item.setName("Some Name");
        user.setEmail("Some@email.com");

        Pageable pageable = PageRequest.of(from, size, Sort.by("start").descending());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findPastByOtherUser(userId, pageable)).thenReturn(List.of(booking));

        List<BookingDto> actualBookingList = bookingService.getAllBooking(userId, state, isOwner, from, size);

        assertEquals(actualBookingList.size(), 1);
        assertEquals(actualBookingList.get(0).getItem().getName(), item.getName());
        assertEquals(actualBookingList.get(0).getBooker().getEmail(), user.getEmail());
    }

    @Test
    void getAllBooking_whenBookingStateIsUnknown_thenReturnBabRequest() {
        long userId = 1L;
        String state = "UnknownState";
        Boolean isOwner = true;
        int from = 0;
        int size = 20;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        BookingException bookingException = assertThrows(BookingException.class,
                () -> bookingService.getAllBooking(userId, state, isOwner, from, size));
        assertEquals(bookingException.getMessage(), "Unknown state: " + state);
    }
}