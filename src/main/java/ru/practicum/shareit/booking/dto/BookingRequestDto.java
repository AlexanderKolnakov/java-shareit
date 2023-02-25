package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.validation.AfterNow;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BookingRequestDto {

    private Long id;

    @AfterNow(message = "Дата начала бронирования не может быть в прошлом.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    @AfterNow(message = "Дата окончания бронирования не может быть в прошлом.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;

    private Long bookerId;

    private Status status;

    private Long itemId;
}
