package ru.practicum.shareit.item.comment.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.CommentDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentDtoTest {

    @Test
    void testNoArgsConstructor() {
        CommentDto commentDto = new CommentDto();

        assertNull(commentDto.getId());
        assertNull(commentDto.getText());
        assertNull(commentDto.getAuthorName());
        assertNull(commentDto.getCreated());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String text = "Test comment";
        String authorName = "User";
        LocalDateTime created = LocalDateTime.of(2023, 1, 1, 12, 0);

        CommentDto commentDto = new CommentDto(id, text, authorName, created);

        assertEquals(id, commentDto.getId());
        assertEquals(text, commentDto.getText());
        assertEquals(authorName, commentDto.getAuthorName());
        assertEquals(created, commentDto.getCreated());
    }

    @Test
    void testSettersAndGetters() {
        CommentDto commentDto = new CommentDto();

        Long id = 1L;
        String text = "Test comment";
        String authorName = "User";
        LocalDateTime created = LocalDateTime.of(2023, 1, 1, 12, 0);

        commentDto.setId(id);
        commentDto.setText(text);
        commentDto.setAuthorName(authorName);
        commentDto.setCreated(created);

        assertEquals(id, commentDto.getId());
        assertEquals(text, commentDto.getText());
        assertEquals(authorName, commentDto.getAuthorName());
        assertEquals(created, commentDto.getCreated());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime created = LocalDateTime.of(2023, 1, 1, 12, 0);

        CommentDto commentDto1 = new CommentDto(1L, "Comment", "User", created);
        CommentDto commentDto2 = new CommentDto(1L, "Comment", "User", created);
        CommentDto commentDto3 = new CommentDto(2L, "Comment", "User", created);

        assertEquals(commentDto1, commentDto2);
        assertEquals(commentDto1.hashCode(), commentDto2.hashCode());

        assertNotEquals(commentDto1, commentDto3);
        assertNotEquals(commentDto1.hashCode(), commentDto3.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime created = LocalDateTime.of(2023, 1, 1, 12, 0);
        CommentDto commentDto = new CommentDto(1L, "Comment", "User", created);

        String result = commentDto.toString();

        assertEquals("CommentDto(id=1, text=Comment, authorName=User, created=2023-01-01T12:00)", result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("text=Comment"));
    }
}
