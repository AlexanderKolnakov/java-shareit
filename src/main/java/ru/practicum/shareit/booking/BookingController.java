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
import java.util.List;


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
    public BookingDto changeBookingStatus (@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long bookingId,
                              @RequestParam("approved") Boolean approved) {
        log.info("Получен PATCH запрос на обновление статуса бронирование с id: " + bookingId +
                " от пользователя с id: " + userId);
        return bookingService.changeBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        log.debug("Получен GET запрос на получение информации о бронировании с id: " +
                bookingId + " от пользователя с id: {}.", userId);
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getAllBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(name = "state", defaultValue = "ALL",
                                                  required = false) State state) {
//        switch (state) {
//            case ALL:
//                log.debug("Получен GET запрос на получение информации обо всех бронированиях пользователя с id: {}.", userId);
//                return bookingService.getAllBooking(userId);
//            case PAST:
//                return null;
//            default:
//                return null;
//        }
        log.debug("Получен GET запрос на получение информации обо всех бронированиях пользователя с id: {}.", userId);
        return bookingService.getAllBooking(userId, state);
    }
}
