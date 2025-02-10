package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Archinski
 */
@Component
public class CommentMapper {

    public static CommentDTO commentToDto(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Попытка конвертировать comment == null");
        }
        Integer author = comment.getAuthor().getId();
        String authorImage = comment.getAuthor().getImage().getImagePath();
        String authorFirstName = comment.getAuthor().getFirstname();
        LocalDateTime createdAt = comment.getCreatedAt();
        Integer pk = comment.getPk();
        String text = comment.getText();
        return new CommentDTO(author, authorImage, authorFirstName, createdAt, pk, text);
    }

    public static Comment commentDtoToComment(CommentDTO commentDTO) {
        if (commentDTO == null) {
            throw new IllegalArgumentException("Попытка конвертировать commentDTO == null");
        }
        Comment comment = new Comment();

        comment.setPk(commentDTO.getPk());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        comment.setText(commentDTO.getText());

        return comment;
    }

    public static CommentsDTO toCommentsDTO(List<Comment> comments) {
        CommentsDTO commentsDTO = new CommentsDTO();
        commentsDTO.setCount(comments.size());
        commentsDTO.setResults(comments.stream().map(e -> commentToDto(e)).collect(Collectors.toList()));
        return commentsDTO;
    }
}
