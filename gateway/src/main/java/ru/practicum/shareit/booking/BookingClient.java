package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.client.BaseClient;

import java.net.URLEncoder;
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

    public ResponseEntity<Object> getBookings(long userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }


    public ResponseEntity<Object> createBooking(long bookerId, BookingRequestDto bookingRequestDto) {
        return post("", bookerId, bookingRequestDto);
    }
    public ResponseEntity<Object> changeBookingStatus(long userId, long bookingId, boolean approved) {
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId,
                "approved", approved
        );
        return patch("/" + bookingId + "?approved=" + approved, userId);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBooking(long userId, State state, boolean false, int from, int size) {
        return get("/" + bookingId, userId);
    }




    public ResponseEntity<Object> fefefefe (long userId, String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
//                "state", state.name(),
//                "from", from,
//                "size", size
        );
        return get("/search?text=" + URLEncoder.encode(text) + "&from=" + from + "&size=" + size, userId, parameters);
    }


}
