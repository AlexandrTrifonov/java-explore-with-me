package ru.practicum.mainservice.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainservice.comment.model.CommentModel;
import ru.practicum.mainservice.comment.model.Count;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentModel, Long> {

    List<CommentModel> findAllByAuthorId(Long userId, Pageable pageable);

    List<CommentModel> findAllByEventId(Long eventId, Pageable pageable);

    Long countByEventId(Long eventId);

    @Query("select new ru.practicum.mainservice.comment.model.Count(" +
            "event.id, count(c)) from CommentModel c group by c.event.id")
    List<Count> getCommentsCountByEvent();
}
