package ru.practicum.mainservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.event.enums.State;
import ru.practicum.mainservice.event.model.EventModel;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<EventModel, Long> {

    List<EventModel> findAllByInitiatorId(Long userId, Pageable pageable);

    EventModel findByIdAndInitiatorId(Long eventId, Long userId);

    List<EventModel> findAllByIdIn(List<Long> eventsId);

    @Query("SELECT e FROM EventModel e " +
            "WHERE (e.initiator.id IN (:users) OR :users IS NULL) " +
            "AND (e.state IN (:states) OR :states IS NULL) " +
            "AND (e.categoryModel.id IN (:categories) OR :categories IS NULL) " +
            "AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd) " +
            "ORDER BY e.createdOn DESC")
    Page<EventModel> getEventsByAdmin(
            @Param("users") List<Long> users,
            @Param("states") List<State> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    @Query("SELECT e " +
            "FROM EventModel e " +
            "WHERE ((LOWER(e.annotation) LIKE CONCAT('%',lower(:text),'%') " +
            "OR LOWER(e.description) LIKE CONCAT('%',lower(:text),'%')) " +
            "OR :text IS NULL ) " +
            "AND (e.categoryModel.id IN (:categories) OR :categories IS NULL) " +
            "AND (e.paid IN (:paid) OR :paid IS NULL) " +
            "AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd)")
    Page<EventModel> getEventsByPublic(@Param("text") String text,
                                       @Param("categories") List<Long> categories,
                                       @Param("paid") Boolean paid,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable pageable);

    EventModel findByIdAndState(Long eventId, State state);

    @Query("SELECT e " +
            "FROM EventModel e " +
            "WHERE (e.categoryModel.id IN (:categoryId))")
    List<EventModel> findAllByCategoryId(Long categoryId);
}
