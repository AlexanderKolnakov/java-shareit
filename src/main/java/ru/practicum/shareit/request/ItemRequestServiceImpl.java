package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private final ItemRepository itemRepository;


    @Override
    @Transactional(rollbackOn = Exception.class)
    public ItemRequestDto createItemRequest(Long userId, ItemRequest itemRequest) {
        checkUserId(userId);
        itemRequest.setUserRequest(userId);
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.itemRequestToDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> findUserItemRequest(Long userId) {
        checkUserId(userId);
        List<ItemRequest> itemRequestList = itemRequestRepository.searchItemRequestByUserId(userId);
        List<ItemRequestDto> itemRequestDtoList = ItemRequestMapper.mapItemRequestToDto(itemRequestList);
        for (ItemRequestDto itemRequestDto : itemRequestDtoList) {
            setItemForRequest(itemRequestDto);
        }
        itemRequestDtoList.sort(Comparator.comparing(ItemRequestDto::getCreated).reversed());
        return itemRequestDtoList;
    }

    @Override
    public List<ItemRequestDto> findAllItemRequest(Long userId, int from, int size) {
        checkUserId(userId);

        try {
            Pageable pageable = PageRequest.of(from, size, Sort.by("created").descending());
            Page<ItemRequest> itemRequestList = itemRequestRepository.findAll(pageable);

            List<ItemRequestDto> result = ItemRequestMapper
                    .mapItemRequestToDto(itemRequestList.stream().filter(e -> !e.getUserRequest().equals(userId))
                            .collect(Collectors.toList()));

            for (ItemRequestDto itemRequestDto : result) {
                setItemForRequest(itemRequestDto);
            }
            return result;


        } catch (IllegalArgumentException e) {
            throw new DataIntegrityViolationException("Не правильно указаны индексы искомых запросов: "
                    + from + " и " + size);
        }
    }

    @Override
    public ItemRequestDto findItemRequest(Long userId, Long requestId) {
        checkUserId(userId);
        checkRequestId(requestId);

        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);
        ItemRequestDto result = itemRequest.map(ItemRequestMapper::itemRequestToDto).orElseThrow();
        setItemForRequest(result);
        return result;
    }

    private void checkRequestId(Long requestId) {
        if (itemRepository.searchItemByRequestId(requestId).isEmpty()) {
            throw new EntityNotFoundException("Запроса с id " + requestId + " не существует");
        }
        ;
    }

    private void checkUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Пользователя с id " + userId + " не существует");
        }
    }

    private void setItemForRequest(ItemRequestDto itemRequestDto) {
        List<ItemDto> itemList = ItemMapper
                .mapToItemDtoWithRequest(itemRepository.searchItemByRequestId(itemRequestDto.getId()));
        itemRequestDto.setItems(itemList);
    }
}
