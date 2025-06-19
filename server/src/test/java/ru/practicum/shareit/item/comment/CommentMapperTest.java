package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    @Test
    void toCommentDto_whenValidComment_thenCorrectDto() {
        User author = new User(1L, "Author", "author@example.com");
        Item item = new Item();
        item.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(Instant.now());

        CommentDto result = CommentMapper.toCommentDto(comment);

        assertEquals(1L, result.getId());
        assertEquals("Test comment", result.getText());
        assertEquals("Author", result.getAuthorName());
        assertNotNull(result.getCreated());
    }

    @Test
    void toComment_whenValidDto_thenCorrectEntity() {
        CommentDto dto = new CommentDto();
        dto.setId(1L);
        dto.setText("Test comment");
        dto.setAuthorName("Author");
        dto.setCreated(LocalDateTime.now());

        Comment result = CommentMapper.toComment(dto);

        assertEquals(1L, result.getId());
        assertEquals("Test comment", result.getText());

        assertNull(result.getAuthor());
        assertNull(result.getItem());
        assertNotNull(result.getCreated());
    }

    @Test
    void toCommentDto_whenNullComment_thenReturnsNull() {
        CommentDto result = CommentMapper.toCommentDto(null);

        assertNull(result);
    }

    @Test
    void toComment_whenNullDto_thenReturnsNull() {
        Comment result = CommentMapper.toComment(null);

        assertNull(result);
    }
}
