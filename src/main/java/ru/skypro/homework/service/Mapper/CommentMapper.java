package ru.skypro.homework.service.Mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comments.CommentDTO;
import ru.skypro.homework.dto.Comments.CreateOrUpdateComment;
import ru.skypro.homework.entity.Advertisement;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdvertisementRepository;
import ru.skypro.homework.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserRepository userRepository;
    private final AdvertisementRepository adRepository;


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

    public Comment toEntity(CreateOrUpdateComment dto, UserEntity author, Advertisement ad) {
        return Comment.builder()
                .author(author)
                .ad(ad)
                .createdAt(System.currentTimeMillis())
                .text(dto.getText())
                .build();
    }

}
