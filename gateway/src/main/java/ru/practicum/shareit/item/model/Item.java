package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Item {

    @Positive(message = "Некорректный номер id.")
    private Long id;

    @NotBlank(message = "Вы не указали имя вещи")
    private String name;

    @Length(max = 200, message = "Длина описания не должна превышать 200 символов.")
    @NotBlank(message = "Вы не указали описание вещи")
    private String description;

    @NotNull(message = "Вы не указали статус: доступна или вещь для аренды или нет")
    private Boolean available;

    private Long owner;

    private Long requestId;

}
