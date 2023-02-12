package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserCreateDto {

    private Long id;

    private String name;

    @NotBlank(message = "Введите email.")
    @Email(message = "Введен не корректный email.")
    private String email;

}
