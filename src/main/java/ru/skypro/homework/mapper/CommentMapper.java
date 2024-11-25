package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CommentMapper {

    private final UserRepository userRepository;
    private final AdRepository adRepository;

    //из дто в сущность
    public CommentEntity toCommentEntity(CreateOrUpdateComment createOrUpdateComment,
                                         String username,
                                         int id) {
        if (createOrUpdateComment == null) {
            throw new NullPointerException("Переданный объект comment is null");
        }

        UserEntity userEntity = userRepository.findByUsername(username);
        AdEntity adEntity = adRepository.findAdEntityById(id);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setAuthor(userEntity.getId());
        commentEntity.setAuthorImage(userEntity.getImage());
        commentEntity.setAuthorFirstName(userEntity.getFirstName());
        commentEntity.setCreatedAt(Instant.now().toEpochMilli());
        commentEntity.setText(createOrUpdateComment.getTextComment());
        commentEntity.setUser(userEntity);
        commentEntity.setAd(adEntity);
        return commentEntity;
    }

    //из сущности в дто Comment
    public Comment toCommentDto(CommentEntity commentEntity) {
        if (commentEntity == null) {
            throw new NotFoundException("Переданный объект commentEntity is null");
        }
        Comment comment = new Comment();
        comment.setAuthor(commentEntity.getAuthor());
        comment.setAuthorImage(commentEntity.getAuthorImage());
        comment.setAuthorFirstName(commentEntity.getAuthorFirstName());
        comment.setCreatedAt(commentEntity.getCreatedAt());
        comment.setPk(commentEntity.getCommentId());
        comment.setText(commentEntity.getText());
        return comment;
    }
}
