package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItem_IdIn(List<Long> itemIds);

    default Map<Long, List<Comment>> findCommentByItemIds(List<Long> itemIds) {
        return findAllByItem_IdIn(itemIds).stream()
                .collect(Collectors.groupingBy(c -> c.getItem().getId()));
    }

    List<Comment> findCommentByItemId(Long itemId);
}