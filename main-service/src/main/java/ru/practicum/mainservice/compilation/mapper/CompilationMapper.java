package ru.practicum.mainservice.compilation.mapper;

import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.CompilationModel;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.model.EventModel;

import java.util.List;

public class CompilationMapper {

    public static CompilationModel toCompilationModel(NewCompilationDto newCompilationDto, List<EventModel> events) {
        return CompilationModel.builder()
                .id(null)
                .events(events)
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static CompilationDto toCompilationDto(CompilationModel compilationModel, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilationModel.getId())
                .title(compilationModel.getTitle())
                .pinned(compilationModel.getPinned())
                .events(events)
                .build();
    }
}
