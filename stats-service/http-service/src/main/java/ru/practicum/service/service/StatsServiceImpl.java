package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.mapper.StatsMapper;
import ru.practicum.service.repository.StatsRepository;
import ru.practicum.service.exception.InvalidDateException;

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
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
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

