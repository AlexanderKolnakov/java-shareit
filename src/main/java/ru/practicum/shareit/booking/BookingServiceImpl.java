package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional(rollbackOn = Exception.class)
    public BookingDto createBooking(final @NotNull Long bookerId,
                                    final @NotNull BookingRequestDto bookingRequestDto) {

        checkBookerId(bookerId);
        checkBookingDateTime(bookingRequestDto);

        final Item item = itemRepository.getReferenceById(bookingRequestDto.getItemId());

        checkItemId(item);
        checkAvailable(item);
        checkBookerIsOwner(bookerId, item.getOwner());

        bookingRequestDto.setBookerId(bookerId);
        bookingRequestDto.setStatus(Status.WAITING);

        final User user = userRepository.getReferenceById(bookerId);
        final Booking booking = BookingMapper.toBookingFromBookingRequestDto(bookingRequestDto, item, user);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public BookingDto changeBookingStatus(Long userId, Long bookingId, Boolean approved) {

        checkMayUserApprovedBooking(userId, bookingId);
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (approved) {
            checkBookingAlreadyApproved(bookingId);
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        checkBookerId(userId);
        checkBookingId(bookingId);
        checkMayUserGetBookingInfo(userId, bookingId);
        return BookingMapper.toBookingDto(bookingRepository.findById(bookingId)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public List<BookingDto> getAllBooking(Long userId, String state, Boolean isOwner, int from, int size) {
        checkBookerId(userId);
        try {
            Pageable pageableCheck = PageRequest.of(from, size);
        } catch (IllegalArgumentException e) {
            throw new DataIntegrityViolationException("Не правильно указаны индексы искомых запросов: "
                    + from + " и " + size);
        }
        Pageable pageable = PageRequest.of((from / size), size, Sort.by("start").descending());

        final State stateCorrect = parseStatus(state);
        List<Booking> bookingsPage;

        switch (stateCorrect) {
            case ALL:
                if (isOwner) {
                    bookingsPage = bookingRepository.findAllByOwner(userId, pageable);
                } else {
                    bookingsPage = bookingRepository.findAllByOtherUser(userId, pageable);
                }
                return BookingMapper.mapToBookingDto(bookingsPage);

            case FUTURE:

                if (isOwner) {
                    bookingsPage = bookingRepository.findFutureByOwner(userId, pageable);
                } else {
                    bookingsPage = bookingRepository.findFutureByOtherUser(userId, pageable);
                }
                return BookingMapper.mapToBookingDto(bookingsPage);

            case WAITING:
                if (isOwner) {
                    bookingsPage = bookingRepository.findByStatusAndByOwner(userId, Status.WAITING, pageable);
                } else {
                    bookingsPage = bookingRepository.findByStatusAndByOtherUser(userId, Status.WAITING, pageable);
                }
                return BookingMapper.mapToBookingDto(bookingsPage);

            case REJECTED:
            case CURRENT:
                if (isOwner) {
                    bookingsPage = bookingRepository.findByStatusAndByOwner(userId, Status.REJECTED, pageable);
                } else {
                    bookingsPage = bookingRepository.findByStatusAndByOtherUser(userId, Status.REJECTED, pageable);
                }
                return BookingMapper.mapToBookingDto(bookingsPage);

            case PAST:
                if (isOwner) {
                    bookingsPage = bookingRepository.findPastByOwner(userId, pageable);
                } else {
                    bookingsPage = bookingRepository.findPastByOtherUser(userId, pageable);
                }
                return BookingMapper.mapToBookingDto(bookingsPage);

            default:
                throw new BookingException("Unknown state: " + state);
        }
    }

    private State parseStatus(final @NotNull String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            final String errorMsg = "Unknown state: " + state;
            throw new BookingException(errorMsg);
        }
    }

    private void checkAvailable(Item item) {
        if (item.getAvailable().equals(false)) {
            throw new BookingException("Вещь с id " + item.getId() + " сейчас недоступна для бронирования.");
        }
    }

    private void checkBookerId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Пользователя с id " + userId + " не существует");
        }
    }

    private void checkItemId(final @NotNull Item item) {
        if (item.getId() == null) {
            throw new EntityNotFoundException("Вещи с id " + item.getId() + " не существует");
        }
    }

    private void checkBookingId(Long bookingId) {
        if (bookingRepository.findById(bookingId).isEmpty()) {
            throw new EntityNotFoundException("Бронирования с id " + bookingId + " не существует");
        }
    }

    private void checkBookingDateTime(BookingRequestDto booking) {
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BookingException("Дата конца бронирования не может быть раньше чем ее начало.");
        }
    }

    private void checkMayUserApprovedBooking(Long userId, Long bookingId) {
        bookingRepository.findById(bookingId)
                .map(Booking::getItem)
                .map(Item::getOwner)
                .filter(aLong -> aLong.equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id " + userId + " не может подтвердить бронирование этой вещи, " +
                        "так как не является ее владельцем"));
    }

    private void checkMayUserGetBookingInfo(Long userId, Long bookingId) {
        Booking checkBooking = bookingRepository.findById(bookingId).orElseThrow();
        if (!(checkBooking.getItem().getOwner().equals(userId)
                || checkBooking.getBooker().getId().equals(userId))) {
            throw new EntityNotFoundException("Пользователь с id " + userId + " не может получить информацию " +
                    "о бронировании с id  " + bookingId + " так как не является ее владельцем или арендатором");
        }
    }

    private void checkBookingAlreadyApproved(Long bookingId) {
        if (bookingRepository.findById(bookingId).orElseThrow().getStatus().equals(Status.APPROVED)) {
            throw new BookingException("Статус бронирования с id " + bookingId + " уже подтвержден.");
        }
    }

    private void checkBookerIsOwner(Long bookerId, Long owner) {
        if (Objects.equals(bookerId, owner)) {
            throw new EntityNotFoundException("Владелец вещи не может взять ее же в аренду.");
        }
    }
}
