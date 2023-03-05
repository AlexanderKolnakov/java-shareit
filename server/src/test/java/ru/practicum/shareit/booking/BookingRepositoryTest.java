//package ru.practicum.shareit.booking;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import ru.practicum.shareit.booking.model.Booking;
//import ru.practicum.shareit.booking.model.Status;
//import ru.practicum.shareit.item.ItemRepository;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.user.UserRepository;
//import ru.practicum.shareit.user.model.User;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static java.lang.Thread.sleep;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@DataJpaTest
//class BookingRepositoryTest {
//
//    @Autowired
//    private BookingRepository bookingRepository;
//    @Autowired
//    private ItemRepository itemRepository;
//    @Autowired
//    private UserRepository userRepository;
//
//    Item itemOne = new Item();
//    Item itemTwo = new Item();
//    User userOne = new User();
//    User userTwo = new User();
//    Booking bookingOne = new Booking();
//    Booking bookingTwo = new Booking();
//    Booking bookingThree = new Booking();
//    Pageable pageable = PageRequest.of(0, 20, Sort.by("start").descending());
//
//    @BeforeEach
//    private void addBooking() {
//        userOne.setName("User One");
//        userOne.setEmail("One@Email.com");
//
//        userOne = userRepository.save(userOne);
//
//        userTwo.setName("User Two");
//        userTwo.setEmail("Two@Email.com");
//
//        userTwo = userRepository.save(userTwo);
//
//        itemOne.setName("One");
//        itemOne.setDescription("description One");
//        itemOne.setAvailable(true);
//        itemOne.setOwner(userOne.getId());
//        itemOne.setRequestId(userOne.getId());
//
//        itemOne = itemRepository.save(itemOne);
//
//        itemTwo.setName("Two");
//        itemTwo.setDescription("description Two");
//        itemTwo.setAvailable(true);
//        itemTwo.setOwner(userTwo.getId());
//        itemTwo.setRequestId(userTwo.getId());
//
//        itemTwo = itemRepository.save(itemTwo);
//
//        bookingOne.setStart(LocalDateTime.now().plusMinutes(2));
//        bookingOne.setEnd(LocalDateTime.now().plusMinutes(3));
//        bookingOne.setItem(itemOne);
//        bookingOne.setBooker(userOne);
//        bookingOne.setStatus(Status.APPROVED);
//
//        bookingOne = bookingRepository.save(bookingOne);
//
//        bookingTwo.setStart(LocalDateTime.now().plusMinutes(2));
//        bookingTwo.setEnd(LocalDateTime.now().plusMinutes(3));
//        bookingTwo.setItem(itemTwo);
//        bookingTwo.setBooker(userTwo);
//        bookingTwo.setStatus(Status.APPROVED);
//
//        bookingTwo = bookingRepository.save(bookingTwo);
//
//        bookingThree.setStart(LocalDateTime.now().plusSeconds(1));
//        bookingThree.setEnd(LocalDateTime.now().plusSeconds(1));
//        bookingThree.setItem(itemOne);
//        bookingThree.setBooker(userTwo);
//        bookingThree.setStatus(Status.REJECTED);
//
//        bookingThree = bookingRepository.save(bookingThree);
//    }
//
//    @AfterEach
//    private void deleteAll() {
//        itemRepository.deleteAll();
//        userRepository.deleteAll();
//        bookingRepository.deleteAll();
//    }
//
//    @Test
//    void searchBookingByItemId() {
//        final List<Booking> result = bookingRepository.searchBookingByItemId(itemOne.getId());
//
//        assertEquals(2, result.size());
//        assertEquals("One", result.get(0).getItem().getName());
//        assertEquals("One", result.get(1).getItem().getName());
//    }
//
//    @Test
//    void searchBookingByItemIdAndUserId() {
//        final List<Booking> result = bookingRepository.searchBookingByItemIdAndUserId(itemOne.getId(), userOne.getId());
//
//        assertEquals(1, result.size());
//        assertEquals("One", result.get(0).getItem().getName());
//    }
//
//    @Test
//    void findAllByOwner() {
//        final List<Booking> result = bookingRepository.findAllByOwner(userOne.getId(), pageable);
//
//        assertEquals(2, result.size());
//        assertEquals(bookingOne.getId(), result.get(0).getId());
//        assertEquals(bookingThree.getId(), result.get(1).getId());
//    }
//
//    @Test
//    void findAllByOtherUser() {
//        final List<Booking> result = bookingRepository.findAllByOtherUser(userTwo.getId(), pageable);
//
//        assertEquals(2, result.size());
//        assertEquals(bookingTwo.getId(), result.get(0).getId());
//        assertEquals(bookingThree.getId(), result.get(1).getId());
//    }
//
//    @Test
//    void findFutureByOwner() throws InterruptedException {
//        sleep(2000);
//        final List<Booking> result = bookingRepository.findFutureByOwner(userOne.getId(), pageable);
//
//        assertEquals(1, result.size());
//        assertEquals(bookingOne.getId(), result.get(0).getId());
//    }
//
//    @Test
//    void findFutureByOtherUser() throws InterruptedException {
//        sleep(2000);
//        final List<Booking> result = bookingRepository.findFutureByOtherUser(userTwo.getId(), pageable);
//
//        assertEquals(1, result.size());
//        assertEquals(bookingTwo.getId(), result.get(0).getId());
//    }
//
//    @Test
//    void findByStatusAndByOwner() {
//        final List<Booking> result = bookingRepository.findByStatusAndByOwner(userOne.getId(), Status.REJECTED, pageable);
//
//        assertEquals(1, result.size());
//        assertEquals(bookingThree.getId(), result.get(0).getId());
//    }
//
//    @Test
//    void findByStatusAndByOtherUser() {
//        final List<Booking> result = bookingRepository.findByStatusAndByOtherUser(userTwo.getId(), Status.APPROVED, pageable);
//
//        assertEquals(1, result.size());
//        assertEquals(bookingTwo.getId(), result.get(0).getId());
//    }
//
//    @Test
//    void findPastByOwner() throws InterruptedException {
//        sleep(2000);
//        final List<Booking> result = bookingRepository.findPastByOwner(userOne.getId(), pageable);
//
//        assertEquals(1, result.size());
//        assertEquals(bookingThree.getId(), result.get(0).getId());
//    }
//
//    @Test
//    void findPastByOtherUser() throws InterruptedException {
//        sleep(2000);
//        final List<Booking> result = bookingRepository.findPastByOtherUser(userTwo.getId(), pageable);
//
//        assertEquals(1, result.size());
//        assertEquals(bookingThree.getId(), result.get(0).getId());
//    }
//}