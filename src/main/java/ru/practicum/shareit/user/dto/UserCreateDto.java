package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserCreateDto {

    private Long id;

    private String name;

    @NotBlank(message = "Введите email.")
    @Email(message = "Введен не корректный email.")
    private String email;

}
