package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.CreateConstraint;
import ru.practicum.shareit.validation.UpdateConstraint;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utility.HttpHeader.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService service;

    private final ItemMapper mapper;

    @GetMapping("/{itemId}")
    public ItemDto getItemInformation(@PathVariable Long itemId) {
        log.info("Начало обработки запроса на получение информации о вещи {}", itemId);
        Item item = service.getItemById(itemId);
        log.info("Окончание обработки запроса на получение информации о вещи {}", itemId);
        return mapper.toItemDto(item);
    }

    @GetMapping
    public List<ItemDto> getOwnerItems(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Начало обработки запроса на получение списка вещей пользователя {}", userId);
        List<Item> ownerItems = service.getAllItemsByUserId(userId);
        log.info("Окончание обработки запроса на получение списка вещей пользователя {}", userId);
        return ownerItems.stream().map(mapper::toItemDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Начало обработки запроса на поиск с параметром {}", text);
        List<Item> resultOfSearch = service.searchItems(text);
        log.info("Окончание обработки запроса на поиск с параметром {}", text);
        return resultOfSearch.stream().map(mapper::toItemDto).collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                        @RequestBody @Validated(CreateConstraint.class) ItemDto item) {
        log.info("Начало обработки запроса на добавление вещи пользователем {}", userId);
        Item newItem = service.addItem(userId, mapper.fromItemDto(item));
        log.info("Окончание обработки запроса на добавление вещи пользователем {}", userId);
        return mapper.toItemDto(newItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestHeader(X_SHARER_USER_ID) Long userId,
                              @RequestBody @Validated(UpdateConstraint.class) ItemDto itemDto) {
        log.info("Начало обработки запроса на обновление информации о вещи {}", itemId);
        Item updatedItem = service.updateItem(itemId, userId, itemDto);
        log.info("Окончание обработки запроса на обновление информации о вещи {}", itemId);
        return mapper.toItemDto(updatedItem);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                           @PathVariable Long itemId) {
        log.info("Начало обработки запроса на удаление вещи {}", itemId);
        service.deleteItem(userId, itemId);
        log.info("Окончание обработки запроса на удаление вещи {}", itemId);
    }
}
