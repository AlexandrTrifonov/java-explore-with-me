package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.service.CommentService;
import ru.practicum.mainservice.constants.Constants;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Validated
public class CommentAdminController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getCommentsByAdmin(
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero int from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive int size) {
        return commentService.getCommentsByAdmin(from, size);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(
            @PathVariable Long commentId) {
        commentService.deleteCommentByAdmin(commentId);
    }
}
