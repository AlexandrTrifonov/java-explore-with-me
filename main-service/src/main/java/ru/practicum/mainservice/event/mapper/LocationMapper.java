package ru.practicum.mainservice.event.mapper;

import ru.practicum.mainservice.event.dto.LocationDto;
import ru.practicum.mainservice.event.model.LocationModel;

public class LocationMapper {

    public static LocationModel toLocationModel(LocationDto dto) {
        return LocationModel.builder()
                .id(null)
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }

    public static LocationDto toLocationDto(LocationModel locationModel) {
        return LocationDto.builder()
                .lat(locationModel.getLat())
                .lon(locationModel.getLon())
                .build();
    }
}
