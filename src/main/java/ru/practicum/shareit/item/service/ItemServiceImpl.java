package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utility.ObjectChecker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage storage;

    private final UserStorage userStorage;

    private final BookingStorage bookingStorage;

    private final BookingMapper bookingMapper;

    private final ItemMapper itemMapper;

    private final ObjectChecker objectChecker;

    @Override
    @Transactional
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        objectChecker.userFound(userId);
        Item newItem = itemMapper.fromItemDto(itemDto);
        newItem.setOwner(userStorage.findById(userId).get());
        return itemMapper.toItemDto(storage.save(newItem));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) {
        Item updatedItem = storage.getReferenceById(itemId);
        objectChecker.userAccess(updatedItem.getOwner().getId(), userId);
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            updatedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            updatedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updatedItem.setAvailable(itemDto.getAvailable());
        }
        storage.save(updatedItem);
        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    @Transactional
    public ItemDto getItemById(Long itemId) {
        ItemDto itemDto = itemMapper.toItemDto(storage.getReferenceById(itemId));
        Booking lastBooking = bookingStorage
                .findFirstByItemIdAndStartBeforeOrderByStartDesc(itemId, LocalDateTime.now());
        Booking nextBooking = bookingStorage
                .findFirstByItemIdAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now());
        if (lastBooking != null) {
            itemDto.lastBooking = bookingMapper.toShortBookingDto(lastBooking);
        }
        if (nextBooking != null) {
            itemDto.nextBooking = bookingMapper.toShortBookingDto(nextBooking);
        }
        return itemDto;
    }

    @Override
    @Transactional
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        return storage.findAllByOwnerId(userId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteItem(Long userId, Long itemId) {
        storage.deleteById(itemId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return storage.searchAvailableItems(text.toLowerCase()).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
