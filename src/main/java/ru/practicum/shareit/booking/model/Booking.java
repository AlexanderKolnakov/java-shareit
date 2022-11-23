package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.User;


import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Positive(message = "Некорректный номер id.")
    private Long id;

    private LocalDate start;

    private LocalDate end;

    private Item item;

    private User booker;

    private Status status;
}
