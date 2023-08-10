package ru.practicum.mainservice.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.CategoryModel;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.enums.State;
import ru.practicum.mainservice.event.model.EventModel;
import ru.practicum.mainservice.event.model.LocationModel;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.UserModel;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public EventFullDto toEventFullDto(EventModel eventModel, Long confirmedRequests, Long view) {
        return EventFullDto.builder()
                .annotation(eventModel.getAnnotation())
                .category(CategoryMapper.toCategoryDto(eventModel.getCategoryModel()))
                .confirmedRequests(confirmedRequests)
                .createdOn(eventModel.getCreatedOn())
                .description(eventModel.getDescription())
                .eventDate(eventModel.getEventDate())
                .id(eventModel.getId())
                .initiator(UserMapper.toUserShortDto(eventModel.getInitiator()))
                .location(LocationMapper.toLocationDto(eventModel.getLocation()))
                .paid(eventModel.getPaid())
                .participantLimit(eventModel.getParticipantLimit())
                .publishedOn(eventModel.getPublishedOn())
                .requestModeration(eventModel.getRequestModeration())
                .state(eventModel.getState())
                .title(eventModel.getTitle())
                .views(view)
                .build();
    }

    public EventModel toEventModel(NewEventDto newEventDto, CategoryModel categoryModel, LocationModel locationModel, UserModel initiator) {
        return EventModel.builder()
                .id(null)
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .categoryModel(categoryModel)
                .description(newEventDto.getDescription())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .eventDate(newEventDto.getEventDate())
                .location(locationModel)
                .createdOn(LocalDateTime.now())
                .state(State.PENDING)
                .publishedOn(null)
                .initiator(initiator)
                .requestModeration(newEventDto.getRequestModeration())
                .build();
    }

    public EventShortDto toEventShortDto(EventModel eventModel, Long confirmedRequests, Long view) {
        return EventShortDto.builder()
                .annotation(eventModel.getAnnotation())
                .category(CategoryMapper.toCategoryDto(eventModel.getCategoryModel()))
                .confirmedRequests(confirmedRequests)
                .eventDate(eventModel.getEventDate())
                .id(eventModel.getId())
                .initiator(UserMapper.toUserShortDto(eventModel.getInitiator()))
                .paid(eventModel.getPaid())
                .title(eventModel.getTitle())
                .views(view)
                .build();
    }
}
