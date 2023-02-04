package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long bookerId, BookingRequestDto bookingRequestDto);

    BookingDto changeBookingStatus(Long userId, Long bookingId, Boolean approved);

    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getAllBooking(Long userId, String state, Boolean isOwner);
}
