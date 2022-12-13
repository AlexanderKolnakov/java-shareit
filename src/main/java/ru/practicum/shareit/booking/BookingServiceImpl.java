package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;



    @Override
    public BookingDto createBooking(Long bookerId, Booking booking) {
        checkItemId(booking.getItemId());
        checkAvailable(booking.getItemId());
        checkBookerId(bookerId);
        checkBookingDateTime(booking);
        booking.setBookerId(bookerId);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto changeBookingStatus(Long bookerId, Long bookingId, Boolean approved) {
        return null;
    }


    private void checkAvailable(Long itemId) {
        if (itemRepository.getById(itemId).getAvailable().equals(false)) {
            throw new BookingException("Вещь с id " + itemId + " сейчас недоступна для бронирования.");
        }
    }

    private void checkBookerId(Long bookerId) {
        if (userRepository.findById(bookerId).isEmpty()) {
            throw new EntityNotFoundException("Пользователя с id " + bookerId + " не существует");
        }
    }
    private void checkItemId(Long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new EntityNotFoundException("Вещи с id " + itemId + " не существует");
        }
    }

    private void checkBookingDateTime(Booking booking) {
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BookingException("Дата конца бронирования не может быть раньше чем ее начало.");
        }
    }

}
