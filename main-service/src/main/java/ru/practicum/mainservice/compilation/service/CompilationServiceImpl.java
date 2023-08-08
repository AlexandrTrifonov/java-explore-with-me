package ru.practicum.mainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.dto.UpdateCompilationRequest;
import ru.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.mainservice.compilation.model.CompilationModel;
import ru.practicum.mainservice.compilation.repository.CompilationRepository;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.model.EventModel;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.exception.NotFoundException;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final EventService eventService;
    private final CompilationRepository compilationRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<EventModel> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventService.getEventsByIdsList(newCompilationDto.getEvents());
            if (newCompilationDto.getEvents().size() != events.size()) {
                throw new NotFoundException("События не найдены");
            }
        }
        CompilationModel newCompilation = compilationRepository.save(CompilationMapper.toCompilationModel(newCompilationDto, events));
        log.info("Новая подборка сохранена");
        return getCompilationById(newCompilation.getId());
    }

    @Override
    public void deleteCompilationById(Long compId) {
        getCompilationById(compId);
        log.info("Подборка удалена");
        compilationRepository.deleteById(compId);
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        CompilationModel compilation = getCompilationModelById(compId);
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        if (updateCompilationRequest.getEvents() != null) {
            List<EventModel> events = eventService.getEventsByIdsList(updateCompilationRequest.getEvents());

            if (updateCompilationRequest.getEvents().size() != events.size()) {
                throw new NotFoundException("События не найдены");
            }

            compilation.setEvents(events);
        }
        compilationRepository.save(compilation);
        return getCompilationById(compId);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        List<CompilationModel> compilations;
        PageRequest pageable = PageRequest.of(from / size, size);
        if (pinned == null) {
            compilations = compilationRepository.findAll(pageable).toList();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        }
        List<CompilationDto> listCompilations = new ArrayList<>();
        compilations.forEach(compilation -> {
            List<EventShortDto> eventsShortDto = eventService.getListEventsShortDto(compilation.getEvents());
            listCompilations.add(CompilationMapper.toCompilationDto(compilation, eventsShortDto));
        });
        return listCompilations;
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        CompilationModel compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборки не существует"));
        List<EventShortDto> eventsShortDto = eventService.getListEventsShortDto(compilation.getEvents());
        return CompilationMapper.toCompilationDto(compilation, eventsShortDto);
    }

    private CompilationModel getCompilationModelById(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборки не существует"));
    }
}
