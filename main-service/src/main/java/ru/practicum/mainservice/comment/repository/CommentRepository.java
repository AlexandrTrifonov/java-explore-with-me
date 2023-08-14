package ru.practicum.mainservice.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.comment.model.CommentModel;
import ru.practicum.mainservice.comment.model.Count;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentModel, Long> {

    List<CommentModel> findAllByAuthorId(Long userId, Pageable pageable);

    List<CommentModel> findAllByEventId(Long eventId, Pageable pageable);

    Long countByEventId(Long eventId);

    @Query("SELECT new ru.practicum.mainservice.comment.model.Count(event.id, count(c)) " +
            "from CommentModel c " +
            "WHERE (c.event.id IN (:eventsId) OR :eventsId IS NULL) " +
            "group by c.event.id")
    List<Count> getCommentsCountByEvent(@Param("eventsId") List<Long> eventsId);
}
