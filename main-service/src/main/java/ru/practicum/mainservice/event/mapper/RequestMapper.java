package ru.practicum.mainservice.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.event.dto.ParticipationRequestDto;
import ru.practicum.mainservice.event.model.RequestModel;

@UtilityClass
public class RequestMapper {

    public ParticipationRequestDto toParticipationRequestDto(RequestModel requestModel) {
        return ParticipationRequestDto.builder()
                .id(requestModel.getId())
                .event(requestModel.getEvent().getId())
                .requester(requestModel.getRequester().getId())
                .created(requestModel.getCreated())
                .status(requestModel.getStatus())
                .build();
    }
}
