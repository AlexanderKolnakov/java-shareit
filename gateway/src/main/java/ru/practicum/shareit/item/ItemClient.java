package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(long ownerId, Item item) {
        return post("", ownerId, item);
    }

    public ResponseEntity<Object> updateItem(long ownerId, long itemId, ItemDto itemUpdateDto) {
        return patch("/" + itemId, ownerId, itemUpdateDto);
    }

    public ResponseEntity<Object> findAllItem(long ownerId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/", ownerId, parameters);
    }

    public ResponseEntity<Object> getUserItem(long ownerId, long itemId) {
        return get("/" + itemId, ownerId);
    }

    public ResponseEntity<Object> getItemSearchByDescription(String text, int from, int size, long userId) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text=" + text + "&from=" + from + "&size=" + size, userId, parameters);
    }

    public ResponseEntity<Object> createComment(long authorId, long itemId, Comment comment) {
        return post("/" + itemId + "/comment", authorId, comment);
    }
}
