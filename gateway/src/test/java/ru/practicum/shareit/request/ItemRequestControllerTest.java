//package ru.practicum.shareit.request;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.shareit.request.model.ItemRequest;
//
//import static org.hamcrest.Matchers.is;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(ItemRequestController.class)
//class ItemRequestControllerTest {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ItemRequestClient itemRequestService;
//
//    /*@Test
//    @SneakyThrows
//    void createItemRequest_whenItemRequestValid_thenReturnItemRequest() {
//        long userId = 1L;
//        ItemRequest itemRequestCreate = new ItemRequest();
//        itemRequestCreate.setDescription("Some Description");
//
//        when(itemRequestService.createItemRequest(any(), any()))
//                .thenReturn(ItemRequestMapper.itemRequestToDto(itemRequestCreate));
//
//        mockMvc.perform(post("/requests")
//                        .header("X-Sharer-User-Id", userId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(itemRequestCreate)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(itemRequestCreate.getId()), Long.class))
//                .andExpect(jsonPath("$.description", is(itemRequestCreate.getDescription())))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//    }*/
//
////    @Test
////    @SneakyThrows
////    void createItemRequest_whenItemRequestNotValid_thenReturnBadRequest() {
////        ItemRequest itemRequestCreate = new ItemRequest();
////        long userId = 1L;
////        when(itemRequestService.createItemRequest(any(), any()))
////                .thenReturn(ItemRequestMapper.itemRequestToDto(itemRequestCreate));
////
////        String result = mockMvc.perform(post("/requests")
////                        .header("X-Sharer-User-Id", userId)
////                        .contentType("application/json")
////                        .content(objectMapper.writeValueAsString(itemRequestCreate)))
////                .andExpect(status().isBadRequest())
////                .andReturn()
////                .getResponse()
////                .getContentAsString();
////        assertEquals("Вы не указали описание вещи", result);
////    }
//
//    /*@Test
//    @SneakyThrows
//    void findAllUsersItemRequest() {
//        long userId = 1L;
//        mockMvc.perform(get("/requests")
//                        .header("X-Sharer-User-Id", userId))
//                .andDo(print())
//                .andExpect(status().isOk());
//        verify(itemRequestService).findUserItemRequest(userId);
//    }
//
//    @Test
//    @SneakyThrows
//    void findAllItemRequest() {
//        long userId = 1L;
//        int from = 0;
//        int size = 20;
//
//        mockMvc.perform(get("/requests/all?from=" + from + "&size=" + size)
//                        .header("X-Sharer-User-Id", userId))
//                .andDo(print())
//                .andExpect(status().isOk());
//        verify(itemRequestService).findAllItemRequest(userId, from, size);
//    }
//
//    @Test
//    @SneakyThrows
//    void findItemRequest() {
//        long userId = 1L;
//        long requestId = 1;
//
//        mockMvc.perform(get("/requests/" + requestId)
//                        .header("X-Sharer-User-Id", userId))
//                .andDo(print())
//                .andExpect(status().isOk());
//        verify(itemRequestService).findItemRequest(userId, requestId);
//    }*/
//}