package ru.practicum.mainservice.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.event.dto.LocationDto;
import ru.practicum.mainservice.event.model.LocationModel;

@UtilityClass
public class LocationMapper {

    public LocationModel toLocationModel(LocationDto dto) {
        return LocationModel.builder()
                .id(null)
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }

    public LocationDto toLocationDto(LocationModel locationModel) {
        return LocationDto.builder()
                .lat(locationModel.getLat())
                .lon(locationModel.getLon())
                .build();
    }
}
