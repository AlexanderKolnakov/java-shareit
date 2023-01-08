package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Query("select b from Booking b " +
            "where b.item.id = ?1 ")
    List<Booking> searchBookingByItemId(Long itemId);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and b.booker.id = ?2 and b.status = 'APPROVED' ")
    List<Booking> searchBookingByItemIdAndUserId(Long itemId, Long userI);
}
