package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;



    @Override
    public BookingDto createBooking(Long bookerId, BookingRequestDto bookingRequestDto) {
        checkBookerId(bookerId);
        checkItemId(itemRepository.getById(bookingRequestDto.getItemId()));
        checkAvailable(itemRepository.getById(bookingRequestDto.getItemId()));
        checkBookingDateTime(bookingRequestDto);

        bookingRequestDto.setBookerId(bookerId);
        bookingRequestDto.setStatus(Status.WAITING);

        User user = userRepository.getReferenceById(bookerId);
        Item item = itemRepository.getReferenceById(bookingRequestDto.getItemId());

        Booking booking = BookingMapper.toBookingFromBookingRequestDto(bookingRequestDto, item, user);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto changeBookingStatus(Long userId, Long bookingId, Boolean approved) {

        checkMayUserApprovedBooking(userId, bookingId);
        Booking booking = bookingRepository.getById(bookingId);
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        checkBookerId(userId);
        checkBookingId(bookingId);
        checkMayUserGetBookingInfo(userId, bookingId);
        return BookingMapper.toBookingDto(bookingRepository.getById(bookingId));
    }

    @Override
    public List<BookingDto> getAllBooking(Long userId, State state) {
        checkBookerId(userId);
        return BookingMapper.mapToBookingDto(bookingRepository.findAll()
                .stream().sorted(Comparator.comparing(Booking::getId).reversed()).collect(Collectors.toList()));
    }


    private void checkAvailable(Item item) {
        if (item.getAvailable().equals(false)) {
            throw new BookingException("Вещь с id " + item.getId() + " сейчас недоступна для бронирования.");
        }
    }

    private void checkBookerId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Пользователя с id " + userId + " не существует");
        }
    }
    private void checkItemId(Item item) {
        if (item.getId() == null) {
            throw new EntityNotFoundException("Вещи с id " + item.getId() + " не существует");
        }
    }

    private void checkBookingId(Long bookingId) {
        if (bookingRepository.findById(bookingId).isEmpty()) {
            throw new EntityNotFoundException("Бронирования с id " + bookingId + " не существует");
        }
    }



    private void checkBookingDateTime(BookingRequestDto booking) {
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BookingException("Дата конца бронирования не может быть раньше чем ее начало.");
        }
    }

    private void checkMayUserApprovedBooking(Long userId, Long bookingId) {
        if (!(bookingRepository.getById(bookingId).getItem().getOwner().equals(userId))) {
            throw new BookingException("Пользователь с id " + userId + " не может подтвердить бронирование этой вещи, " +
                    "так как не является ее владельцем");
        }
    }

    private void checkMayUserGetBookingInfo(Long userId, Long bookingId) {
        if (!(bookingRepository.getById(bookingId).getItem().getOwner().equals(userId)
                || bookingRepository.getById(bookingId).getBooker().getId().equals(userId))) {
            throw new BookingException("Пользователь с id " + userId + " не может получить информацию " +
                    "о бронировании с id  " + bookingId + " так как не является ее владельцем или арендатором");
        }
    }

}
