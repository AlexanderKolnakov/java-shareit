package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Query("select b from Booking b " +
            "where b.item.id = ?1 ")
    List<Booking> searchBookingByItemId(Long itemId);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and b.booker.id = ?2 and b.status = 'APPROVED' ")
    List<Booking> searchBookingByItemIdAndUserId(Long itemId, Long userI);

    @Query("select b from Booking b " +
            "where b.item.owner = ?1")
    Page<Booking> findAllByOwner(Long userId, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1")
    Page<Booking> findAllByOtherUser(Long userId, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.item.owner = ?1 and b.start > current_timestamp()")
    Page<Booking> findFutureByOwner(Long userId, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 and b.start > current_timestamp()")
    Page<Booking> findFutureByOtherUser(Long userId, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.item.owner = ?1 and b.status = ?2")
    Page<Booking> findByStatusAndByOwner(Long userId, Status status, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 and b.status = ?2")
    Page<Booking> findByStatusAndByOtherUser(Long userId, Status status, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.item.owner = ?1 and b.end < current_timestamp()")
    Page<Booking> findPastByOwner(Long userId, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 and b.end < current_timestamp()")
    Page<Booking> findPastByOtherUser(Long userId, Pageable pageable);
}
