package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class CommentRepositoryDefaultMethodsTest {

    @Test
    void findCommentByItemIds_shouldGroupCommentsByItemId() {
        CommentRepository repository = Mockito.mock(CommentRepository.class, Mockito.CALLS_REAL_METHODS);

        User author = new User(1L, "Author", "a@b.c");
        Item item1 = new Item();
        item1.setId(1L);
        Item item2 = new Item();
        item2.setId(2L);

        Comment c1 = new Comment(1L, "text1", item1, author, null);
        Comment c2 = new Comment(2L, "text2", item1, author, null);
        Comment c3 = new Comment(3L, "text3", item2, author, null);

        when(repository.findAllByItem_IdIn(anyList())).thenReturn(List.of(c1, c2, c3));

        Map<Long, List<Comment>> result = repository.findCommentByItemIds(List.of(1L, 2L));

        assertEquals(2, result.size());
        assertEquals(List.of(c1, c2), result.get(1L));
        assertEquals(List.of(c3), result.get(2L));

        verify(repository).findAllByItem_IdIn(anyList());
    }
}