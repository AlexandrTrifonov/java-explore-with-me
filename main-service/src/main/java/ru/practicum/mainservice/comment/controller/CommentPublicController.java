package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping
    public List<CommentShortDto> getCommentsByPublic(
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero int from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive int size) {
        return commentService.getCommentsByPublic(from, size);
    }
}
