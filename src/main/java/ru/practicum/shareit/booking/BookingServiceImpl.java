package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;



    @Override
    public BookingDto createBooking(Long bookerId, BookingRequestDto bookingRequestDto) {

        User user = userRepository.getReferenceById(bookerId);
        Item item = itemRepository.getReferenceById(bookingRequestDto.getItemId());

        checkBookerId(bookerId);
        checkItemId(item);
        checkAvailable(item);
        checkBookingDateTime(bookingRequestDto);
        checkBookerIsOwner(bookerId, item.getOwner());

        bookingRequestDto.setBookerId(bookerId);
        bookingRequestDto.setStatus(Status.WAITING);

        Booking booking = BookingMapper.toBookingFromBookingRequestDto(bookingRequestDto, item, user);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    private void checkBookerIsOwner(Long bookerId, Long owner) {
        if (bookerId.equals(owner)) {
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
    public List<BookingDto> getAllBooking(Long userId, String state) {

        checkBookerId(userId);
        try {
            State stateCorrect = State.valueOf(state);

            switch (stateCorrect) {
                case ALL:
                    return BookingMapper.mapToBookingDto(bookingRepository.findAll()
                            .stream().sorted(Comparator.comparing(Booking::getId).reversed()).collect(Collectors.toList()));
                case FUTURE:
                    return BookingMapper.mapToBookingDto(bookingRepository.findAll()
                            .stream().filter(e -> e.getStart().isAfter(LocalDateTime.now()))
                            .sorted(Comparator.comparing(Booking::getId).reversed()).collect(Collectors.toList()));
                default:
                    throw new BookingException("Unknown state: " + state);
            }

        } catch (IllegalArgumentException e) {
            throw new BookingException("Unknown state: " + state);
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
    private void checkItemId(Item item) {
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
        if (!(bookingRepository.getById(bookingId).getItem().getOwner().equals(userId))) {
            throw new EntityNotFoundException("Пользователь с id " + userId + " не может подтвердить бронирование этой вещи, " +
                    "так как не является ее владельцем");
        }
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
