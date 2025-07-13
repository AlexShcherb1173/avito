package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdvertDTO.UserShortDTO;
import ru.skypro.homework.dto.CommentDTO.CommentCreateDTO;
import ru.skypro.homework.dto.CommentDTO.CommentDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/ads/{advertId}/comments")
public class CommentController {
    // Временное хранилище комментариев (заглушка)
    private final List<CommentDTO> stubComments = new ArrayList<>();
    private final AtomicLong commentIdCounter = new AtomicLong(1);

    public CommentController() {
        // Инициализация тестовых данных
        initializeStubData();
    }

    private void initializeStubData() {
        UserShortDTO author = new UserShortDTO();
        author.setId(1L);
        author.setUsername("stubUser");
        author.setAvatar("https://example.com/avatar1.jpg");

        CommentDTO comment = new CommentDTO();
        comment.setId(commentIdCounter.getAndIncrement());
        comment.setText("Интересное предложение, можно ли посмотреть вживую?");
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now().minusHours(3));
        stubComments.add(comment);
    }

    /**
     * Заглушка для получения комментариев к объявлению
     * @param advertId ID объявления
     * @return Список комментариев
     */
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAdvertComments(@PathVariable Long advertId) {
        return ResponseEntity.ok(stubComments);
    }

    /**
     * Заглушка для добавления комментария
     * @param advertId ID объявления
     * @param createDTO Текст комментария
     * @return Созданный комментарий
     */
    @PostMapping
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long advertId,
            @RequestBody CommentCreateDTO createDTO) {
        UserShortDTO author = new UserShortDTO();
        author.setId(1L);
        author.setUsername("currentUser");
        author.setAvatar("https://example.com/current_avatar.jpg");

        CommentDTO newComment = new CommentDTO();
        newComment.setId(commentIdCounter.getAndIncrement());
        newComment.setText(createDTO.getText());
        newComment.setAuthor(author);
        newComment.setCreatedAt(LocalDateTime.now());
        stubComments.add(newComment);

        return ResponseEntity.ok(newComment);
    }

    /**
     * Заглушка для удаления комментария
     * @param commentId ID комментария
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        stubComments.removeIf(c -> c.getId().equals(commentId));
        return ResponseEntity.noContent().build();
    }
}
