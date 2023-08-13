package ru.practicum.mainservice.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.CommentShortDto;
import ru.practicum.mainservice.comment.dto.NewCommentDto;
import ru.practicum.mainservice.comment.model.CommentModel;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.user.mapper.UserMapper;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public CommentDto toCommentDto(CommentModel comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .commentText(comment.getCommentText())
                .createdOn(comment.getCreatedOn())
                .author(UserMapper.toUserShortDto(comment.getAuthor()))
                .event(EventMapper.toEventShortDto(comment.getEvent(), 0L, 0L, 0L))
                .build();
    }

    public CommentModel toCommentModel(NewCommentDto newCommentDto) {
        return CommentModel.builder()
                .commentText(newCommentDto.getCommentText())
                .createdOn(LocalDateTime.now())
                .build();
    }

    public CommentShortDto toCommentShortDto(CommentModel comment) {
        return CommentShortDto.builder()
                .id(comment.getId())
                .commentText(comment.getCommentText())
                .createdOn(comment.getCreatedOn())
                .event(EventMapper.toEventShortDto(comment.getEvent(), 0L, 0L, 0L))
                .build();
    }
}
