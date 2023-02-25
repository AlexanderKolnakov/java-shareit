package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {

    private Long id;

    private String name;

    @Email(message = "Введен не корректный email.")
    private String email;

}
