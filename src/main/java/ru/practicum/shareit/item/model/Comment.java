package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMMENTS")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive(message = "Некорректный номер id.")
    private Long id;

    @Length(max = 400, message = "Длина комментария не должна превышать 400 символов.")
    @NotBlank(message = "Вы не написали комментарий.")
    @Column(name = "COMMENT_TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User author;

    @Column(name = "CREATED_DATE")
    private LocalDateTime created;
}
