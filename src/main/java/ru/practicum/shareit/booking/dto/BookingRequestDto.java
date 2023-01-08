package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.validation.AfterNow;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingRequestDto {

    private Long id;

    @AfterNow(message = "Дата начала бронирования не может быть в прошлом.")
    private LocalDateTime start;

    @AfterNow(message = "Дата окончания бронирования не может быть в прошлом.")
    private LocalDateTime end;

    private Long bookerId;

    private Status status;

    private Long itemId;
}
