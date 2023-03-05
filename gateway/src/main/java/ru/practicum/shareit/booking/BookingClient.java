package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long bookerId, BookingRequestDto bookingRequestDto) {
        return post("", bookerId, bookingRequestDto);
    }

    public ResponseEntity<Object> changeBookingStatus(long userId, long bookingId, boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBooking(long userId, String state, boolean required, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "required", required,
                "from", from,
                "size", size
        );
        try {
            Pageable pageableCheck = PageRequest.of(from, size);
        } catch (IllegalArgumentException e) {
            throw new DataIntegrityViolationException("Не правильно указаны индексы искомых запросов: "
                    + from + " и " + size);
        }

        if (required) {
            return get("/owner?state=" + state + "&from=" + from + "&size=" + size, userId, parameters);
        } else {
            return get("/?state=" + state+ "&from=" + from + "&size=" + size, userId, parameters);
        }
    }
}
