package ru.practicum.shareit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Positive(message = "Некорректный номер id.")
    private Long id;

    @NotBlank(message = "Введите email.")
    @Email(message = "Введен не корректный email.")
    private String email;

    private String name;
}
