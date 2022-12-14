package ru.practicum.shareit.booking.model;

import lombok.*;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Hibernate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.AfterNow;
import ru.practicum.shareit.validation.BeforeNow;


import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BOOKINGS")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive(message = "Некорректный номер id.")
    private Long id;

    @Column(name = "START_DATE")
    @AfterNow(message = "Дата начала бронирования не может быть в прошлом.")
    private LocalDateTime start;

    @Column(name = "END_DATE")
    @AfterNow(message = "Дата окончания бронирования не может быть в прошлом.")
    private LocalDateTime end;

    @OneToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @OneToOne
    @JoinColumn(name = "BOOKER_ID")
    private User booker;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Booking booking = (Booking) o;
        return id != null && Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


//    @Positive(message = "Некорректный номер id.")
//    private Long id;
//
//    private LocalDate start;
//
//    private LocalDate end;
//
//    private Item item;
//
//    private User booker;
//
//    private Status status;
}
