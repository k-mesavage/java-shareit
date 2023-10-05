package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.CreateConstraint;
import ru.practicum.shareit.validation.UpdateConstraint;


import static ru.practicum.shareit.utility.HttpHeader.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                     @PathVariable Long itemId) {
        log.info("Get item by id {}", itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        log.info("Find all items by user id {}", userId);
        return itemClient.findAllItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                              @RequestHeader(X_SHARER_USER_ID) Long userId,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Find items by request");
        return itemClient.findItemsByRequest(text, userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                          @RequestBody @Validated(CreateConstraint.class) ItemDto item) {
        log.info("Add item by user id {}", userId);
        return itemClient.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId,
                                             @RequestHeader(X_SHARER_USER_ID) Long userId,
                                             @RequestBody @Validated(UpdateConstraint.class) ItemDto gatewayItemDto) {
        log.info("Update item by id {}", itemId);
        return itemClient.updateItem(gatewayItemDto, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                             @PathVariable Long itemId) {
        log.info("Delete item by id {}", itemId);
        itemClient.deleteItem(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Validated (CreateConstraint.class) CommentDto gatewayCommentDto) {
        log.info("User {} add comment for Item {}", userId, itemId);
        return itemClient.addComment(gatewayCommentDto, itemId, userId);
    }
}
