package ru.practicum.mainservice.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.CommentShortDto;
import ru.practicum.mainservice.comment.dto.NewCommentDto;
import ru.practicum.mainservice.comment.mapper.CommentMapper;
import ru.practicum.mainservice.comment.model.CommentModel;
import ru.practicum.mainservice.comment.repository.CommentRepository;
import ru.practicum.mainservice.event.model.EventModel;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.exception.InvalidEventDateException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.user.model.UserModel;
import ru.practicum.mainservice.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventService eventService;

    @Override
    public CommentDto createCommentPrivate(Long userId, Long eventId, NewCommentDto newCommentDto) {
        UserModel user = userService.getUserById(userId);
        EventModel event = eventService.getEventById(eventId);
        CommentModel newCommentModel = CommentMapper.toCommentModel(newCommentDto);
        newCommentModel.setAuthor(user);
        newCommentModel.setEvent(event);
        commentRepository.save(newCommentModel);
        log.info("PRIVATE - Создан новый комментарий {}", newCommentModel);
        return CommentMapper.toCommentDto(newCommentModel);
    }

    @Override
    public CommentDto updateCommentByPrivate(Long userId, Long commentId, NewCommentDto newCommentDto) {
        log.info("Запуск метода updateCommentByPrivate");
        UserModel user = userService.getUserById(userId);
        CommentModel comment = getCommentById(commentId);
        validateCommentAuthor(user, comment);
        if (newCommentDto.getCommentText() != null && !newCommentDto.getCommentText().isBlank()) {
            comment.setCommentText(newCommentDto.getCommentText());
        }
        log.info("PRIVATE - Обновление комментария {}", commentId);
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteCommentByPrivate(Long userId, Long commentId) {
        log.info("Запуск метода deleteCommentByPrivate");
        UserModel user = userService.getUserById(userId);
        CommentModel comment = getCommentById(commentId);
        validateCommentAuthor(user, comment);
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getCommentsByPrivate(Long userId, int from, int size) {
        userService.getUserById(userId);
        PageRequest pageable = PageRequest.of(from / size, size);
        List<CommentModel> comments = commentRepository.findAllByAuthorId(userId, pageable);
        log.info("PRIVATE - Список комментариев пользователя с id {}", userId);
        return getListCommentsDto(comments);
    }

    @Override
    public List<CommentShortDto> getCommentsByPublic(int from, int size) {
        PageRequest pageable = PageRequest.of(from / size, size);
        Page<CommentModel> commentsPage = commentRepository.findAll(pageable);
        List<CommentModel> comments = commentsPage.getContent();
        log.info("PUBLIC - Список всех комментариев");
        return getListCommentsShortDto(comments);
    }

    @Override
    public List<CommentDto> getCommentsByAdmin(int from, int size) {
        PageRequest pageable = PageRequest.of(from / size, size);
        Page<CommentModel> commentsPage = commentRepository.findAll(pageable);
        List<CommentModel> comments = commentsPage.getContent();
        log.info("ADMIN - Список всех комментариев");
        return getListCommentsDto(comments);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        log.info("Запуск метода deleteCommentByAdmin");
        CommentModel comment = getCommentById(commentId);
        commentRepository.deleteById(commentId);
    }

    private CommentModel getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не существует."));
    }

    private void validateCommentAuthor(UserModel user, CommentModel comment) {
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new InvalidEventDateException("Комментарий может редактировать/удалять только автор комментария");
        }
    }

    private List<CommentDto> getListCommentsDto(List<CommentModel> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private List<CommentShortDto> getListCommentsShortDto(List<CommentModel> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentShortDto)
                .collect(Collectors.toList());
    }
}
