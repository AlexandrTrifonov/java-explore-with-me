package ru.practicum.mainservice.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.comment.model.CommentModel;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentModel, Long> {

    List<CommentModel> findAllByAuthorId(Long userId, Pageable pageable);
}
