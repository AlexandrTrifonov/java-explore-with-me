package ru.practicum.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.HitDto;
import ru.practicum.service.model.StatsModel;

@UtilityClass
public class StatsMapper {
    public StatsModel toModel(HitDto hitDto) {
        StatsModel statsModel = new StatsModel();
        statsModel.setApp(hitDto.getApp());
        statsModel.setUri(hitDto.getUri());
        statsModel.setIp(hitDto.getIp());
        statsModel.setTimestamp(hitDto.getTimestamp());
        return statsModel;
    }
}
