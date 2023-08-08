package ru.practicum.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.Constants;
import ru.practicum.dto.HitDto;
import ru.practicum.service.model.StatsModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class StatsMapper {
    public static StatsModel toModel(HitDto hitDto) {
        StatsModel statsModel = new StatsModel();
        statsModel.setApp(hitDto.getApp());
        statsModel.setUri(hitDto.getUri());
        statsModel.setIp(hitDto.getIp());
        statsModel.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)));
        return statsModel;
    }
}
