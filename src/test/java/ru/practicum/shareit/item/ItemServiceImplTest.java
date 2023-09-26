package ru.practicum.shareit.item;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utility.ObjectChecker;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemStorage itemStorage;
    @Mock
    private BookingStorage bookingStorage;
    @Mock
    private CommentStorage commentStorage;
    @Mock
    private ObjectChecker objectChecker;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private ItemServiceImpl itemService;

    private final User actualUser = new User(1L, "User name", "email@mail.com");
    private final Item actualItem = Item.builder()
            .id(1L)
            .name("Item name")
            .description("Item description")
            .owner(actualUser)
            .available(true)
            .build();
    private final ItemDto actualItemDto = ItemDto.builder()
            .id(1L)
            .name("Item name")
            .description("Item description")
            .available(true)
            .build();

    @Nested
    class CreateTests {
        @Test
        void addItem() {
            lenient().when(itemMapper.fromItemDto(any()))
                    .thenReturn(actualItem);
            lenient().when(userStorage.getReferenceById(anyLong()))
                    .thenReturn(actualUser);
            lenient().when(itemMapper.toItemDto(any()))
                    .thenReturn(actualItemDto);

            ItemDto expectedItem = itemService.addItem(1L, actualItemDto);
            assertEquals(expectedItem.getId(), actualItem.getId());
            assertEquals(expectedItem.getName(), "Item name");
            verify(itemStorage).save(actualItem);
        }

        @Test
        void updateItem() {
            when(itemStorage.getReferenceById(anyLong()))
                    .thenReturn(actualItem);
            when(itemMapper.updateItem(any(), any()))
                    .thenReturn(actualItem);
            when(itemMapper.toItemDto(actualItem))
                    .thenReturn(actualItemDto);

            ItemDto expectedItem = itemService.updateItem(1L, 1L, actualItemDto);
            assertEquals(expectedItem.getId(), actualItem.getId());
            verify(itemStorage).save(actualItem);
        }

        @Test
        void addComment() {
            Comment actualComment = Comment.builder()
                    .id(1L)
                    .item(actualItem)
                    .author(actualUser)
                    .text("Text of comment")
                    .build();
            CommentDto actualCommentDto = CommentDto.builder()
                    .text("Text of comment")
                    .id(1L)
                    .authorName(actualUser.getName())
                    .build();
            when(commentMapper.toComment(any(), any(), any(), any()))
                    .thenReturn(actualComment);
            when(commentMapper.toCommentDto(any()))
                    .thenReturn(actualCommentDto);

            CommentDto expectedCommentDto = itemService.addComment(1L, 1L, actualCommentDto);
            assertEquals(expectedCommentDto.getText(), "Text of comment");
            verify(commentStorage).save(actualComment);
        }
    }

    @Nested
    class GetTest {
        @Test
        void getItemById() {
            when(itemStorage.getReferenceById(anyLong()))
                    .thenReturn(actualItem);
            when(itemMapper.toItemDto(any()))
                    .thenReturn(actualItemDto);
            when(bookingMapper.addShortBooking(any()))
                    .thenReturn(actualItemDto);
            ItemDto expectedItem = itemService.getItemById(1L, 1L);
            assertEquals(expectedItem.getId(), actualItem.getId());
        }

        @Test
        void getAllItemsByUserId() {
            when(itemStorage.findAllByOwnerId(anyLong(), any()))
                    .thenReturn(List.of(actualItem));
            when(itemMapper.toItemDto(any()))
                    .thenReturn(actualItemDto);
            when(bookingMapper.addShortBooking(any()))
                    .thenReturn(actualItemDto);

            List<ItemDto> expectedItems = itemService.getAllItemsByUserId(1L, 1, 10);
            assertEquals(expectedItems.size(), 1);
            assertEquals(expectedItems.get(0), actualItemDto);
        }

        @Test
        void searchItems() {
            when(itemStorage.searchAvailableItems(anyString()))
                    .thenReturn(List.of(actualItem));
            when(itemMapper.toItemDto(any()))
                    .thenReturn(actualItemDto);
            List<ItemDto> expectedItems = itemService.searchItems("Some text");
            assertEquals(expectedItems.size(), 1);
            assertEquals(expectedItems.get(0).getId(), actualItem.getId());
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void deleteItem() {
            itemService.deleteItem(1L, 1L);
            verify(itemStorage).deleteById(anyLong());
        }
    }
}