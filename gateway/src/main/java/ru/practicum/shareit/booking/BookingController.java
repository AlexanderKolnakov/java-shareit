package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.Valid;


@RestController
@Slf4j
@AllArgsConstructor
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        log.debug("Получен POST запрос на бронирование (booking) вещи");
        return bookingClient.createBooking(bookerId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> changeBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @PathVariable Long bookingId,
                                                      @RequestParam("approved") Boolean approved) {
        log.info("Получен PATCH запрос на обновление статуса бронирование с id: " + bookingId +
                " от пользователя с id: " + userId);
        return bookingClient.changeBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        log.debug("Получен GET запрос на получение информации о бронировании с id: " +
                bookingId + " от пользователя с id: {}.", userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL",
                                                        required = false) String state,
                                                @RequestParam(name = "from", defaultValue = "0") int from,
                                                @RequestParam(name = "size", defaultValue = "20") int size) {
        log.debug("Получен GET запрос на получение информации обо всех бронированиях пользователя с id: {}.", userId);
        return bookingClient.getAllBooking(userId, state, false, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(name = "state", defaultValue = "ALL",
                                                               required = false) String state,
                                                       @RequestParam(name = "from", defaultValue = "0") int from,
                                                       @RequestParam(name = "size", defaultValue = "20") int size) {
        log.debug("Получен GET запрос на получение информации обо всех бронированиях владельца с id: {}.", userId);
        return bookingClient.getAllBooking(userId, state, true, from, size);
    }
}
