package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.impl.CommentServiceImpl;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии", description = "Методы для работы с комментариями")
public class CommentController implements CommentControllerInterface {
    private final CommentServiceImpl commentServiceImpl;

    public CommentController(CommentServiceImpl commentServiceImpl) {
        this.commentServiceImpl = commentServiceImpl;
    }

    @GetMapping("/{id}/comments")
    @Override
    public CommentsDto getComments(@PathVariable(name = "id") Integer id) {
        return commentServiceImpl.getAllCommentByAdsId(id);
    }

    @PostMapping("/{id}/comments")
    @PreAuthorize("hasRole('USER')")
    @Override
    public CommentDto addComment(@PathVariable(name = "id") Integer id, @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        return commentServiceImpl.addComment(id, createOrUpdateCommentDto);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.findComment(#commentId).author.email == authentication.name")
    @Override
    public void deleteComment(@PathVariable(name = "adId") Integer adId, @PathVariable(name = "commentId") Integer commentId) {
        commentServiceImpl.removeComment(adId, commentId);
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.findComment(#commentId).author.email == authentication.name")
    @Override
    public CommentDto updateComment(@PathVariable(name = "adId") Integer adId, @PathVariable(name = "commentId") Integer commentId, @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        return commentServiceImpl.updateComment(adId, commentId, createOrUpdateCommentDto);
    }
}
