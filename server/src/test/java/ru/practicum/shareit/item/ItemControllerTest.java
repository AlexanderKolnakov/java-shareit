package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    @SneakyThrows
    void createItem_whenItemValid_thenReturnItem() {
        long ownerId = 1L;
        Item item = new Item();
        item.setId(1L);
        item.setName("Some Name");
        item.setDescription("Some Description");
        item.setAvailable(true);
        item.setOwner(ownerId);

        when(itemService.createItem(ownerId, item))
                .thenReturn(ItemMapper.toItemDto(item));

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", ownerId)
                        .content(objectMapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));
    }

//    @Test
//    @SneakyThrows
//    void createItem_whenItemNotValidName_thenReturnBadRequest() {
//        long ownerId = 1L;
//        Item item = new Item();
//        item.setDescription("Some Description");
//        item.setAvailable(true);
//        item.setOwner(ownerId);
//
//        ItemDto itemDto = new ItemDto();
//
//        when(itemService.createItem(ownerId, item))
//                .thenReturn(itemDto);
//
//        String result = mockMvc.perform(post("/items")
//                        .header("X-Sharer-User-Id", ownerId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(item)))
//                .andExpect(status().isBadRequest())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertEquals("Вы не указали имя вещи", result);
//    }

//    @Test
//    @SneakyThrows
//    void createItem_whenItemNotValidDescription_thenReturnBadRequest() {
//        long ownerId = 1L;
//        Item item = new Item();
//        item.setName("Some Name");
//        item.setAvailable(true);
//        item.setOwner(ownerId);
//
//        ItemDto itemDto = new ItemDto();
//
//        when(itemService.createItem(ownerId, item))
//                .thenReturn(itemDto);
//
//        String result = mockMvc.perform(post("/items")
//                        .header("X-Sharer-User-Id", ownerId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(item)))
//                .andExpect(status().isBadRequest())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertEquals("Вы не указали описание вещи", result);
//    }

//    @Test
//    @SneakyThrows
//    void createItem_whenItemNotValidAvailable_thenReturnBadRequest() {
//        long ownerId = 1L;
//        Item item = new Item();
//        item.setDescription("Some Description");
//        item.setName("Some Name");
//        item.setOwner(ownerId);
//
//        ItemDto itemDto = new ItemDto();
//
//        when(itemService.createItem(ownerId, item))
//                .thenReturn(itemDto);
//
//        String result = mockMvc.perform(post("/items")
//                        .header("X-Sharer-User-Id", ownerId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(item)))
//                .andExpect(status().isBadRequest())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertEquals("Вы не указали статус: доступна или вещь для аренды или нет", result);
//    }

    @Test
    @SneakyThrows
    void updateItem() {
        long itemId = 1L;
        long ownerId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Some Name");
        itemDto.setDescription("Some Description");

        when(itemService.updateItem(ownerId, itemId, itemDto))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", ownerId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void findAllUsersItem() {
        long ownerId = 1L;
        int from = 0;
        int size = 20;

        mockMvc.perform(get("/items?from=" + from + "&size=" + size)
                        .header("X-Sharer-User-Id", ownerId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(itemService).findAllItem(ownerId, from, size);
    }

    @Test
    @SneakyThrows
    void findUserItem() {
        long ownerId = 1L;
        long itemId = 1L;

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", ownerId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(itemService).getUserItem(ownerId, itemId);
    }

    @Test
    @SneakyThrows
    void findItemByDescription_whenTextNotEmpty_thenReturnOkRequest() {
        String text = "SomeSearchText";
        int from = 0;
        int size = 20;

        mockMvc.perform(get("/items/search?text=" + text + "&from=" + from + "&size=" + size))
                .andDo(print())
                .andExpect(status().isOk());
        verify(itemService).getItemSearchByDescription(text, from, size);
    }


    @Test
    @SneakyThrows
    void findItemByDescription_whenTextIsEmpty_thenReturnEmptyList() {
        String text = "";
        int from = 0;
        int size = 20;

        mockMvc.perform(get("/items/search?text=" + text + "&from=" + from + "&size=" + size))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(Collections.EMPTY_LIST)));

        verify(itemService, never()).getItemSearchByDescription(text, from, size);
    }

    @Test
    @SneakyThrows
    void createComment_whenCommentValid_thenReturnComment() {
        long authorId = 1L;
        long itemId = 1L;
        Comment comment = new Comment();
        comment.setText("Some Text");
        CommentDto commentDto = new CommentDto();
        commentDto.setText(comment.getText());

        when(itemService.createComment(any(), any(), any()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .header("X-Sharer-User-Id", authorId)
                        .content(objectMapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(comment.getText())));

        verify(itemService).createComment(any(), any(), any());
    }

//    @Test
//    @SneakyThrows
//    void createComment_whenCommentNotValid_thenBadRequest() {
//        long authorId = 1L;
//        long itemId = 1L;
//        Comment comment = new Comment();
//        CommentDto commentDto = new CommentDto();
//
//        when(itemService.createComment(any(), any(), any()))
//                .thenReturn(commentDto);
//
//        mockMvc.perform(post("/items/" + itemId + "/comment")
//                        .header("X-Sharer-User-Id", authorId)
//                        .content(objectMapper.writeValueAsString(comment))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$", is("Вы не написали комментарий.")));
//
//        verify(itemService, never()).createComment(any(), any(), any());
//    }
}