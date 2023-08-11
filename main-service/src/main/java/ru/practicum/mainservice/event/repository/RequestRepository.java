package ru.practicum.mainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainservice.event.dto.RequestStats;
import ru.practicum.mainservice.event.enums.RequestStatus;
import ru.practicum.mainservice.event.model.RequestModel;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<RequestModel, Long> {

    @Query("SELECT new ru.practicum.mainservice.event.dto.RequestStats(r.event.id, count(r.id)) " +
            "FROM RequestModel AS r " +
            "WHERE r.event.id IN ?1 " +
            "AND r.status = 'CONFIRMED' " +
            "GROUP BY r.event.id")
    List<RequestStats> getConfirmedRequests(List<Long> eventsId);

    List<RequestModel> findAllByEventId(Long eventId);

    List<RequestModel> findAllByIdIn(List<Long> requestIds);

    List<RequestModel> findAllByRequesterId(Long requesterId);

    Optional<RequestModel> findByEventIdAndRequesterId(Long eventId, Long userId);

    List<RequestModel> findAllByEventIdAndStatus(Long eventId, RequestStatus status);
}
