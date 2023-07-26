package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.StatsModel;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<StatsModel, Long> {

    @Query("SELECT new ru.practicum.ViewStatsDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM StatsModel as s " +
            "WHERE s.timestamp BETWEEN :start and :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ViewStatsDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM StatsModel as s " +
            "WHERE s.timestamp BETWEEN :start and :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStatsDto> getStatsUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ViewStatsDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM StatsModel as s " +
            "WHERE s.timestamp BETWEEN :start and :end " +
            "AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStatsDto> getStatsUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ViewStatsDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM StatsModel as s " +
            "WHERE s.timestamp BETWEEN :start and :end " +
            "AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStatsDto> getStatsUrisAndUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
