package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.BeforeNow;


import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Positive(message = "Некорректный номер id.")
    private Long id;

    @BeforeNow(message = "Некорректная дата рождения.")
    private LocalDate start;

    @BeforeNow(message = "Некорректная дата рождения.")
    private LocalDate end;

    private Item item;

    private User booker;

    private Status status;
}
