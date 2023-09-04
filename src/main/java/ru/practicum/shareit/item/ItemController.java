package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.CreateConstraint;
import ru.practicum.shareit.validation.UpdateConstraint;

import java.util.List;

import static ru.practicum.shareit.utility.HttpHeader.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService service;

    @GetMapping("/{itemId}")
    public ItemDto getItemInformation(@PathVariable Long itemId) {
        log.info("Начало обработки запроса на получение информации о вещи {}", itemId);
        ItemDto item = service.getItemById(itemId);
        log.info("Окончание обработки запроса на получение информации о вещи {}", itemId);
        return item;
        }

    @GetMapping
    public List<ItemDto> getOwnerItems(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Начало обработки запроса на получение списка вещей пользователя {}", userId);
        List<ItemDto> ownerItems = service.getAllItemsByUserId(userId);
        log.info("Окончание обработки запроса на получение списка вещей пользователя {}", userId);
        return ownerItems;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Начало обработки запроса на поиск с параметром {}", text);
        List<ItemDto> resultOfSearch = service.searchItems(text);
        log.info("Окончание обработки запроса на поиск с параметром {}", text);
        return resultOfSearch;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                        @RequestBody @Validated(CreateConstraint.class) ItemDto item) {
        log.info("Начало обработки запроса на добавление вещи пользователем {}", userId);
        ItemDto newItem = service.addItem(userId, item);
        log.info("Окончание обработки запроса на добавление вещи пользователем {}", userId);
        return newItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestHeader(X_SHARER_USER_ID) Long userId,
                              @RequestBody @Validated(UpdateConstraint.class) ItemDto itemDto) {
        log.info("Начало обработки запроса на обновление информации о вещи {}", itemId);
        ItemDto updatedItem = service.updateItem(itemId, userId, itemDto);
        log.info("Окончание обработки запроса на обновление информации о вещи {}", itemId);
        return updatedItem;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                           @PathVariable Long itemId) {
        log.info("Начало обработки запроса на удаление вещи {}", itemId);
        service.deleteItem(userId, itemId);
        log.info("Окончание обработки запроса на удаление вещи {}", itemId);
    }
}
