package ru.practicum.shareit.item.mapper;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .description(comment.getDescription())
                .authorName(comment.getAuthor().getName())
                .build();
        return commentDto;
    }

    public Comment toComment(CommentDto commentDto, Item item, User user) {
        Comment comment = Comment.builder()
                .description(commentDto.getDescription())
                .item(item)
                .author(user)
                .build();
        return comment;
    }

    public Optional<List<CommentDto>> DtoList(Iterable<Comment> comments) {
        List<CommentDto> result = new ArrayList<>();
        for (Comment comment : comments) {
            result.add(toCommentDto(comment));
        }
        return Optional.of(result);
    }
}