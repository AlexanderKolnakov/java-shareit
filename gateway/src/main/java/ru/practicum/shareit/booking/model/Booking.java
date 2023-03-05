package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.AfterNow;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Booking {

    @Positive(message = "Некорректный номер id.")
    private Long id;

    @AfterNow(message = "Дата начала бронирования не может быть в прошлом.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    @AfterNow(message = "Дата окончания бронирования не может быть в прошлом.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;

    private Item item;

    private User booker;

    private Status status;
}
