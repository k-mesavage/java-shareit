package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.utility.HttpHeader.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItemInformation(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                      @PathVariable Long itemId) {
        log.info("Начало обработки запроса на получение информации о вещи {}", itemId);
        ItemDto itemDto = itemService.getItemById(userId, itemId);
        log.info("Окончание обработки запроса на получение информации о вещи {}", itemId);
        return itemDto;
    }

    @GetMapping
    public List<ItemDto> getOwnerItems(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        log.info("Начало обработки запроса на получение списка вещей пользователя {}", userId);
        List<ItemDto> ownerItems = itemService.getAllItemsByUserId(userId, from, size);
        log.info("Окончание обработки запроса на получение списка вещей пользователя {}", userId);
        return ownerItems;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Начало обработки запроса на поиск с параметром {}", text);
        List<ItemDto> resultOfSearch = itemService.searchItems(text);
        log.info("Окончание обработки запроса на поиск с параметром {}", text);
        return resultOfSearch;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                        @RequestBody ItemDto item) {
        log.info("Начало обработки запроса на добавление вещи пользователем {}", userId);
        ItemDto newItem = itemService.addItem(userId, item);
        log.info("Окончание обработки запроса на добавление вещи пользователем {}", userId);
        return newItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestHeader(X_SHARER_USER_ID) Long userId,
                              @RequestBody ItemDto itemDto) {
        log.info("Начало обработки запроса на обновление информации о вещи {}", itemId);
        ItemDto updatedItem = itemService.updateItem(itemId, userId, itemDto);
        log.info("Окончание обработки запроса на обновление информации о вещи {}", itemId);
        return updatedItem;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                           @PathVariable Long itemId) {
        log.info("Начало обработки запроса на удаление вещи {}", itemId);
        itemService.deleteItem(userId, itemId);
        log.info("Окончание обработки запроса на удаление вещи {}", itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto) {

        CommentDto newComment = itemService.addComment(userId, itemId, commentDto);
        log.info("User {} add comment for Item {}", userId, itemId);
        return newComment;
    }
}
