//package ru.practicum.shareit.item;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import ru.practicum.shareit.item.model.Comment;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.user.UserRepository;
//import ru.practicum.shareit.user.model.User;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@DataJpaTest
//class CommentRepositoryTest {
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private ItemRepository itemRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    Item itemOne = new Item();
//    Item itemTwo = new Item();
//    Comment commentOne = new Comment();
//    Comment commentTwo = new Comment();
//    User userOne = new User();
//    User userTwo = new User();
//
//    @BeforeEach
//    private void addComments() {
//        userOne.setName("User One");
//        userOne.setEmail("One@Email.com");
//
//        userRepository.save(userOne);
//
//        userTwo.setName("User Two");
//        userTwo.setEmail("Two@Email.com");
//        userRepository.save(userTwo);
//
//        itemOne.setName("One");
//        itemOne.setDescription("description One");
//        itemOne.setAvailable(true);
//        itemOne.setOwner(1L);
//        itemOne.setRequestId(1L);
//
//        itemRepository.save(itemOne);
//
//        itemTwo.setName("Two");
//        itemTwo.setDescription("description Two");
//        itemTwo.setAvailable(true);
//        itemTwo.setOwner(1L);
//        itemTwo.setRequestId(1L);
//
//        itemRepository.save(itemTwo);
//
//        commentOne.setText("Some Comment One");
//        commentOne.setItem(itemOne);
//        commentOne.setAuthor(userOne);
//        commentOne.setCreated(LocalDateTime.now());
//
//        commentTwo.setText("Some Comment Two");
//        commentTwo.setItem(itemTwo);
//        commentTwo.setAuthor(userTwo);
//        commentTwo.setCreated(LocalDateTime.now());
//
//        commentRepository.save(commentOne);
//        commentRepository.save(commentTwo);
//    }
//
//    @AfterEach
//    private void deleteAllComment() {
//        commentRepository.deleteAll();
//    }
//
//
//    @Test
//    void searchCommentByItemId() {
//        final List<Comment> result = commentRepository.searchCommentByItemId(itemTwo.getId());
//
//        assertEquals(1, result.size());
//        assertEquals("Two", result.get(0).getItem().getName());
//    }
//}