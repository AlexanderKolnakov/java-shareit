package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    @Positive(message = "Некорректный номер id.")
    private Long id;

    private String name;

    private String email;

}
