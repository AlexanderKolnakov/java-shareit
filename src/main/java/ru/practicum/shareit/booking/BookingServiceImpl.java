package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

    private void checkBookerIsOwner(Long bookerId, Long owner) {
        if (Objects.equals(bookerId, owner)) {
            throw new EntityNotFoundException("Владелец вещи не может взять ее же в аренду.");
        }
    }

    @Override
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
        return BookingMapper.toBookingDto(bookingRepository.getById(bookingId));
    }

    @Override
    public List<BookingDto> getAllBooking(Long userId, String state, Boolean isOwner) {

        checkBookerId(userId);

        final State stateCorrect = parseStatus(state);

        final List<BookingDto> bookingsDto = BookingMapper.mapToBookingDto(bookingRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .collect(Collectors.toList()));

        switch (stateCorrect) {

            case ALL:
                return filterByState(isOwner, userId, bookingsDto,
                        bookingDto -> bookingDto.getItem().getOwner().equals(userId),
                        bookingDto -> true);
//                if (isOwner)
//                    return bookingsDto.stream()
//                            .filter(e -> e.getItem().getOwner().equals(userId))
//                            .collect(Collectors.toList());
//                else
//                    return bookingsDto.stream()
//                            .filter(e -> e.getBooker().getId().equals(userId))
//                            .collect(Collectors.toList());
            case FUTURE:
                return filterByState(isOwner, userId, bookingsDto,
                        bookingDto -> bookingDto.getStart().isAfter(LocalDateTime.now())
                                && bookingDto.getItem().getOwner().equals(userId),
                        bookingDto -> bookingDto.getStart().isAfter(LocalDateTime.now()));
//            if (isOwner)
//                return bookingsDto.stream()
//                        .filter(e -> e.getStart().isAfter(LocalDateTime.now()))
//                        .filter(e -> e.getItem().getOwner().equals(userId)).collect(Collectors.toList());
//            else
//                return bookingsDto.stream()
//                        .filter(e -> e.getStart().isAfter(LocalDateTime.now()))
//                        .filter(e -> e.getBooker().getId().equals(userId)).collect(Collectors.toList());
            case WAITING:
                return filterByState(isOwner, userId, bookingsDto,
                        bookingDto -> bookingDto.getStatus().equals(Status.WAITING),
                        bookingDto -> bookingDto.getStatus().equals(Status.WAITING));
//                if (isOwner)
//                    return bookingsDto.stream()
//                            .filter(e -> e.getStatus().equals(Status.WAITING))
//                            .filter(e -> e.getItem().getOwner().equals(userId)).collect(Collectors.toList());
//                else
//                    return bookingsDto.stream()
//                            .filter(e -> e.getStatus().equals(Status.WAITING))
//                            .filter(e -> e.getBooker().getId().equals(userId)).collect(Collectors.toList());
            case REJECTED:
                return filterByState(isOwner, userId, bookingsDto,
                        bookingDto -> bookingDto.getStatus().equals(Status.REJECTED),
                        bookingDto -> bookingDto.getStatus().equals(Status.REJECTED));
//                if (isOwner)
//                    return bookingsDto.stream()
//                            .filter(e -> e.getStatus().equals(Status.REJECTED))
//                            .filter(e -> e.getItem().getOwner().equals(userId)).collect(Collectors.toList());
//                else
//                    return bookingsDto.stream()
//                            .filter(e -> e.getStatus().equals(Status.REJECTED))
//                            .filter(e -> e.getBooker().getId().equals(userId)).collect(Collectors.toList());
            default:
                throw new BookingException("Unknown state: " + state);
        }
    }

    private List<BookingDto> filterByState(boolean isOwner,
                                           final @NotNull Long userId,
                                           final @NotNull List<BookingDto> bookingsDto,
                                           final @NotNull Predicate<BookingDto> ifOwner,
                                           final @NotNull Predicate<BookingDto> elseOwner) {
        if (isOwner)
            return bookingsDto.stream()
                    .filter(bookingDto -> bookingDto.getItem().getOwner().equals(userId))
                    .filter(ifOwner::test)
                    .collect(Collectors.toList());
        else
            return bookingsDto.stream()
                    .filter(bookingDto -> bookingDto.getBooker().getId().equals(userId))
                    .filter(elseOwner::test)
                    .collect(Collectors.toList());


    }

    private State parseStatus(final @NotNull String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            final String errorMsg = "Unknown state: " + state;
            log.error(errorMsg);
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
        if (!(bookingRepository.getById(bookingId).getItem().getOwner().equals(userId)
                || bookingRepository.getById(bookingId).getBooker().getId().equals(userId))) {
            throw new EntityNotFoundException("Пользователь с id " + userId + " не может получить информацию " +
                    "о бронировании с id  " + bookingId + " так как не является ее владельцем или арендатором");
        }
    }

    private void checkBookingAlreadyApproved(Long bookingId) {
        if (bookingRepository.getById(bookingId).getStatus().equals(Status.APPROVED)) {
            throw new BookingException("Статус бронирования с id " + bookingId + " уже подтвержден.");
        }
    }
}
