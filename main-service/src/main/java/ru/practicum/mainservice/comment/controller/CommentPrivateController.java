package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.NewCommentDto;
import ru.practicum.mainservice.comment.service.CommentService;
import ru.practicum.mainservice.constants.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(
            @PathVariable Long userId,
            @RequestParam Long eventId,
            @Valid @RequestBody NewCommentDto newCommentDto) {
        return commentService.createCommentPrivate(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateCommentByPrivate(
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @Valid @RequestBody NewCommentDto newCommentDto) {
        return commentService.updateCommentByPrivate(userId, commentId, newCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByPrivate(
            @PathVariable Long userId,
            @PathVariable Long commentId) {
        commentService.deleteCommentByPrivate(userId, commentId);
    }

    @GetMapping
    public List<CommentDto> getCommentsByPrivate(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero int from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive int size) {
        return commentService.getCommentsByPrivate(userId, from, size);
    }
}
