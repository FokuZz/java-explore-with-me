package ru.practicum.explore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c from Comment c " +
            "WHERE c.event.id = :eventId " +
            "and c.status = 'PUBLISHED' order by c.created")
    List<Comment> findCommentsByEventId(Long eventId, Pageable pageable);


    @Query("SELECT c from Comment c " +
            "WHERE c.event.id = :eventId " +
            "order by c.created")
    List<Comment> findAllByEventId(Long eventId, Pageable pageable);
}
