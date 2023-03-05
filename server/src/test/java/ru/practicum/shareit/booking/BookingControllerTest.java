//package ru.practicum.shareit.booking;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.shareit.booking.dto.BookingDto;
//import ru.practicum.shareit.booking.dto.BookingRequestDto;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(BookingController.class)
//class BookingControllerTest {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BookingService bookingService;
//
//    @Test
//    @SneakyThrows
//    void createBooking_whenBookingRequestValid_thenReturnBooking() {
//        long bookerId = 1L;
//        BookingRequestDto bookingRequestDto = new BookingRequestDto();
//        bookingRequestDto.setStart(LocalDateTime.now().plusHours(1));
//        bookingRequestDto.setEnd(LocalDateTime.now().plusHours(2));
//        bookingRequestDto.setId(1L);
//        BookingDto bookingDto = new BookingDto();
//        bookingDto.setId(bookingRequestDto.getId());
//
//        when(bookingService.createBooking(any(), any())).thenReturn(bookingDto);
//
//        mockMvc.perform(post("/bookings")
//                        .header("X-Sharer-User-Id", bookerId)
//                        .content(objectMapper.writeValueAsString(bookingRequestDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//    }
//
//    @Test
//    @SneakyThrows
//    void createBooking_whenStartDateNotValid_thenBAdRequest() {
//        long bookerId = 1L;
//        BookingRequestDto bookingRequestDto = new BookingRequestDto();
//        bookingRequestDto.setStart(LocalDateTime.now().minusMinutes(1));
//        bookingRequestDto.setEnd(LocalDateTime.now().plusMinutes(1));
//        BookingDto bookingDto = new BookingDto();
//
//        when(bookingService.createBooking(any(), any()))
//                .thenReturn(bookingDto);
//
//        mockMvc.perform(post("/bookings")
//                        .header("X-Sharer-User-Id", bookerId)
//                        .content(objectMapper.writeValueAsString(bookingRequestDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$", is("Дата начала бронирования не может быть в прошлом.")))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//    }
//
//    @Test
//    @SneakyThrows
//    void createBooking_whenEndDateNotValid_thenBAdRequest() {
//        long bookerId = 1L;
//        BookingRequestDto bookingRequestDto = new BookingRequestDto();
//        bookingRequestDto.setStart(LocalDateTime.now().plusMinutes(1));
//        bookingRequestDto.setEnd(LocalDateTime.now().minusMinutes(1));
//        BookingDto bookingDto = new BookingDto();
//
//        when(bookingService.createBooking(any(), any()))
//                .thenReturn(bookingDto);
//
//        mockMvc.perform(post("/bookings")
//                        .header("X-Sharer-User-Id", bookerId)
//                        .content(objectMapper.writeValueAsString(bookingRequestDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$", is("Дата окончания бронирования не может быть в прошлом.")))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//    }
//
//    @Test
//    @SneakyThrows
//    void changeBookingStatus() {
//        long bookingId = 1L;
//        long userId = 1L;
//        Boolean available = true;
//        BookingDto bookingDto = new BookingDto();
//        bookingDto.setId(1L);
//
//        when(bookingService.changeBookingStatus(userId, bookingId, available))
//                .thenReturn(bookingDto);
//
//        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
//                        .header("X-Sharer-User-Id", userId)
//                        .queryParam("approved", objectMapper.writeValueAsString(available))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));
//    }
//
//    @Test
//    @SneakyThrows
//    void getBooking() {
//        long bookingId = 1L;
//        long userId = 1L;
//
//        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
//                        .header("X-Sharer-User-Id", userId))
//                .andDo(print())
//                .andExpect(status().isOk());
//        verify(bookingService).getBooking(userId, bookingId);
//    }
//
//    @Test
//    @SneakyThrows
//    void getAllBooking() {
//        long userId = 1L;
//        int from = 0;
//        int size = 20;
//        String state = "ALL";
//
//        mockMvc.perform(get("/bookings?from=" + from + "&size=" + size)
//                        .header("X-Sharer-User-Id", userId))
//                .andDo(print())
//                .andExpect(status().isOk());
//        verify(bookingService).getAllBooking(userId, state, false, from, size);
//    }
//
//    @Test
//    @SneakyThrows
//    void getAllBookingByOwner() {
//        long userId = 1L;
//        int from = 0;
//        int size = 20;
//        String state = "ALL";
//
//        mockMvc.perform(get("/bookings/owner?from=" + from + "&size=" + size)
//                        .header("X-Sharer-User-Id", userId))
//                .andDo(print())
//                .andExpect(status().isOk());
//        verify(bookingService).getAllBooking(userId, state, true, from, size);
//    }
//}