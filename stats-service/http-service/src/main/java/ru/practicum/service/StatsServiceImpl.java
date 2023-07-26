package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.InvalidDateException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public void saveHit(HitDto hitDto) {
        statsRepository.save(StatsMapper.toModel(hitDto));
        log.info("Запрос сохранен");
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        validateDate(start, end);
        log.info("Получение статистики по посещениям");
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return statsRepository.getStatsUniqueIp(start, end);
            } else {
                return statsRepository.getStats(start, end);
            }
        } else {
            if (unique) {
                return statsRepository.getStatsUrisAndUniqueIp(start, end, uris);
            } else {
                return statsRepository.getStatsUris(start, end, uris);
            }
        }
    }

    private void validateDate(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new InvalidDateException("Дата - начало после конца");
        }
    }
}

