package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookingDto {

    private Long id;

    private LocalDate start;

    private LocalDate end;

    private Long itemId;

    private Long bookerId;

}
