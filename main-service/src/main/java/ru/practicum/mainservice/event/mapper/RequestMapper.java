package ru.practicum.mainservice.event.mapper;

import ru.practicum.mainservice.event.dto.ParticipationRequestDto;
import ru.practicum.mainservice.event.model.RequestModel;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(RequestModel requestModel) {
        return ParticipationRequestDto.builder()
                .id(requestModel.getId())
                .event(requestModel.getEvent().getId())
                .requester(requestModel.getRequester().getId())
                .created(requestModel.getCreated())
                .status(requestModel.getStatus())
                .build();
    }
}
