package ru.skypro.homework.service.impl.CommentService;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comments.CommentDTO;
import ru.skypro.homework.dto.Comments.CreateorUpdateComment;
import ru.skypro.homework.entity.Advertisement;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.AdvertisementRepository;
import ru.skypro.homework.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Component

public class CommentMapper {
    private final UserRepository userRepository;
    private final AdvertisementRepository adRepository;

    public CommentMapper(UserRepository userRepository, AdvertisementRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    public CommentDTO toDto(Comment comment) {
        return CommentDTO.builder()
                .pk(comment.getId())
                .author(comment.getAuthor().getId())
                .authorImage(comment.getAuthor().getImage())
                .authorFirstName(comment.getAuthor().getFirstName())
                .createdAt(comment.getCreatedAt())
                .text(comment.getText())
                .build();
    }

    public Comment toEntity(CreateorUpdateComment dto, User author, Advertisement ad) {
        return Comment.builder()
                .author(author)
                .ad(ad)
                .createdAt(LocalDateTime.now())
                .text(dto.getText())
                .build();
    }
}
