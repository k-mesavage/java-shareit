package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utility.ObjectChecker;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    private final CommentStorage commentStorage;

    private final BookingMapper bookingMapper;

    private final ItemMapper itemMapper;

    private final CommentMapper commentMapper;

    private final ObjectChecker objectChecker;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        objectChecker.userFound(userId);
        Item newItem = itemMapper.fromItemDto(itemDto);
        newItem.setOwner(userStorage.getReferenceById(userId));
        return itemMapper.toItemDto(itemStorage.save(newItem));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) {
        Item updatedItem = itemStorage.getReferenceById(itemId);
        objectChecker.userAccess(updatedItem.getOwner().getId(), userId);
        updatedItem = itemMapper.updateItem(updatedItem, itemDto);
        itemStorage.save(updatedItem);
        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        objectChecker.itemFound(itemId);
        Item item = itemStorage.getReferenceById(itemId);
        ItemDto itemDto = itemMapper.toItemDto(item);
        setItemComments(itemDto);
        if (item.getOwner().getId().equals(userId)) {
            return bookingMapper.addShortBooking(itemDto);
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        List<ItemDto> items = itemStorage.findAllByOwnerId(userId)
                .stream()
                .map(itemMapper::toItemDto)
                .map(bookingMapper::addShortBooking)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
        for (ItemDto i : items) {
            setItemComments(i);
        }
        return items;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteItem(Long userId, Long itemId) {
        itemStorage.deleteById(itemId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemStorage.searchAvailableItems(text.toLowerCase()).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        LocalDateTime created = LocalDateTime.now();
        objectChecker.userFound(userId);
        objectChecker.itemFound(itemId);
        objectChecker.bookingFound(userId, itemId);
        User author = userStorage.getReferenceById(userId);
        Item item = itemStorage.getReferenceById(itemId);
        Comment comment = commentMapper.toComment(commentDto, item, author, created);
        return commentMapper.toCommentDto(commentStorage.save(comment));
    }

    private void setItemComments(ItemDto itemDto) {
        itemDto.setComments(commentMapper.toDtoList(commentStorage.findAllByItemId(itemDto.getId())));
    }
}
