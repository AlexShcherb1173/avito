package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.mapper.AppMapper;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class CommentController {
    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final AppMapper appMapper;

    @GetMapping("/{adId}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable Integer adId) {
        List<CommentDTO> comments = commentRepository.findAllByAdId(adId).stream()
                .map(appMapper::commentEntityToCommentDTO)
                .collect(Collectors.toList());

        Comments response = new Comments();
        response.setCount(comments.size());
        response.setResults(comments);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{adId}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Integer adId,
            @RequestBody CreateOrUpdateComment comment,
            Authentication authentication
    ) {
        Users author = (Users) authentication.getPrincipal();
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Comment commentEntity = new Comment();
        commentEntity.setText(comment.getText());
        commentEntity.setAuthor(author);
        commentEntity.setAd(ad);
        commentEntity.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(commentEntity);
        return ResponseEntity.ok(appMapper.commentEntityToCommentDTO(savedComment));
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId
    ) {
        commentRepository.deleteByIdAndAdId(commentId, adId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId,
            @RequestBody CreateOrUpdateComment comment
    ) {
        Comment commentEntity = commentRepository.findByIdAndAdId(commentId, adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        commentEntity.setText(comment.getText());
        Comment updatedComment = commentRepository.save(commentEntity);
        return ResponseEntity.ok(appMapper.commentEntityToCommentDTO(updatedComment));
    }
}