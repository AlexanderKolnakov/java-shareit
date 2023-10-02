package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Comment {

    @Positive(message = "Некорректный номер id.")
    private Long id;

    @Length(max = 400, message = "Длина комментария не должна превышать 400 символов.")
    @NotBlank(message = "Вы не написали комментарий.")
    private String text;

    private Item item;

    private User author;

    private LocalDateTime created;
}
