package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {


//    List<Booking> findByItemContaining(Item item);


    @Query("select b from Booking b " +
            "where b.item.id = ?1 ")
    List<Booking> searchBookingByItemId(Long itemId);
}
