package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;


@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping()
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                 @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        log.debug("Получен POST запрос на бронирование (booking) вещи");
        return bookingService.createBooking(bookerId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeBookingStatus (@RequestHeader("X-Sharer-User-Id") Long bookerId,
                              @PathVariable Long bookingId,
                              @RequestParam("approved") Boolean approved) {
        log.debug("Получен PATCH запрос на обновление статуса бронирование с id: " + bookingId +
                " от пользователя с id: " + bookerId);
        return bookingService.changeBookingStatus(bookerId, bookingId, approved);
    }
}
