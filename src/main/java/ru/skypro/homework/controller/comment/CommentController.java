package ru.skypro.homework.controller.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

import java.util.Collections;

@RestController
@RequestMapping("/ads/{adId}/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии")
public class CommentController {

    @GetMapping
    @Operation(summary = "Получение комментариев")
    public Comments getComments(@PathVariable Integer adId) {
        Comments stub = new Comments();
        stub.setCount(0);
        stub.setResults(Collections.emptyList());
        return stub;
    }

    @PostMapping
    @Operation(summary = "Добавление комментария")
    public Comment addComment(
            @PathVariable Integer adId,
            @RequestBody CreateOrUpdateComment comment
    ) {
        Comment stub = new Comment();
        stub.setPk(1);
        stub.setText(comment.getText());
        return stub;
    }
}