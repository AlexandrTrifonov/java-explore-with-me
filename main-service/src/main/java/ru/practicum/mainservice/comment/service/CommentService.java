package ru.practicum.mainservice.comment.service;

import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.CommentShortDto;
import ru.practicum.mainservice.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createCommentPrivate(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateCommentByPrivate(Long userId, Long commentId, NewCommentDto newCommentDto);

    void deleteCommentByPrivate(Long userId, Long commentId);

    List<CommentDto> getCommentsByPrivate(Long userId, int from, int size);

    List<CommentShortDto> getCommentsByPublic(int from, int size);

    List<CommentDto> getCommentsByAdmin(int from, int size);

    void deleteCommentByAdmin(Long commentId);
}
