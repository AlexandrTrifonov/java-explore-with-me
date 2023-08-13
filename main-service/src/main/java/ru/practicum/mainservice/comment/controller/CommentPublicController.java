package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentShortDto;
import ru.practicum.mainservice.comment.service.CommentService;
import ru.practicum.mainservice.constants.Constants;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Validated
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping("/{eventId}")
    public List<CommentShortDto> getCommentsByEventIdByPublic(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero int from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive int size) {
        return commentService.getCommentsByEventIdByPublic(eventId, from, size);
    }
}
