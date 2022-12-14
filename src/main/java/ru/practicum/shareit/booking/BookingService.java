package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingService {
    BookingDto createBooking(Long bookerId, BookingRequestDto bookingRequestDto);

    BookingDto changeBookingStatus(Long bookerId, Long bookingId, Boolean approved);
}
