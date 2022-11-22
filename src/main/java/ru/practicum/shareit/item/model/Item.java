package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Positive(message = "Некорректный номер id.")
    private Long id;

    @NotBlank(message = "Вы не указали имя вещи")
    private String name;

    @Length(max = 200, message = "Длина описания не должна превышать 400 символов.")
    @NotBlank(message = "Вы не указали описание вещи")
    private String description;

    @AssertTrue(message = "Вы не указали статус: доступна или вещь для аренды или нет")
    private boolean available;

    private Long owner;

    private String request;
}
