package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    CommentMapper commentMapper;

    private final User user = new User(1L, "User name", "email@mail.ru");
    private final Item item = Item.builder().id(1L).build();
    private final Comment actualComment = Comment.builder()
            .id(1L)
            .text("Comment text")
            .author(user)
            .created(LocalDateTime.now()).build();
    private final CommentDto actualCommentDto = CommentDto.builder()
            .id(1L)
            .authorName(user.getName())
            .created(LocalDateTime.now())
            .text("Comment text")
            .build();

    @Test
    void toCommentDto() {
        CommentDto expectedComment = commentMapper.toCommentDto(actualComment);
        assertEquals(expectedComment.getId(), actualComment.getId());
        assertEquals(expectedComment.getText(), actualComment.getText());
    }

    @Test
    void toComment() {
        Comment expectedComment = commentMapper.toComment(actualCommentDto, item, user, actualCommentDto.getCreated());
        assertEquals(expectedComment.getText(), actualCommentDto.getText());
        assertEquals(expectedComment.getAuthor(), user);
        assertEquals(expectedComment.getItem(), item);
    }

    @Test
    void toDtoList() {
        List<Comment> comments = List.of(actualComment);
        List<CommentDto> expectedList = commentMapper.toDtoList(comments);
        assertEquals(expectedList.size(), 1);
        assertEquals(expectedList.get(0).getId(), actualComment.getId());
        assertEquals(expectedList.get(0).getText(), actualComment.getText());
    }
}