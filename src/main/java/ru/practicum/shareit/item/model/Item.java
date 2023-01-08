package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ITEMS")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive(message = "Некорректный номер id.")
    private Long id;

    @NotBlank(message = "Вы не указали имя вещи")
    private String name;

    @Length(max = 200, message = "Длина описания не должна превышать 200 символов.")
    @NotBlank(message = "Вы не указали описание вещи")
    private String description;

    @Column(name = "IS_AVAILABLE")
    @NotNull(message = "Вы не указали статус: доступна или вещь для аренды или нет")
    private Boolean available;

    @Column(name = "OWNER_ID")
    private Long owner;

    @Column(name = "REQUEST_ID")
    private String request;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return id != null && Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
