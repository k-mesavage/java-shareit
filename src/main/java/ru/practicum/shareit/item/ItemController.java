package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    ItemService service;

    @GetMapping("/{itemId}")
    public Item getItemInformation(@PathVariable Long itemId) {
        return service.getItemById(itemId);
    }

    @GetMapping
    public List<Item> getOwnerItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.getAllItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam @NotBlank String text) {
        return service.searchItems(text);
    }

    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                        @RequestBody @Valid ItemDto itemDto) {
        return service.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@PathVariable Long itemId,
                           @RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody String params) {
        return service.updateItem(itemId, userId, params);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long itemId) {
        service.deleteItem(userId, itemId);
    }
}
