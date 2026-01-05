package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoMapper {
    public UserEntity toUserEntity(Register register) {
        if (register == null) return null;

        UserEntity entity = new UserEntity();
        entity.setEmail(register.getUsername()); // username = email
        entity.setPassword(register.getPassword());
        entity.setFirstName(register.getFirstName());
        entity.setLastName(register.getLastName());
        entity.setPhone(register.getPhone());
        entity.setRole(register.getRole());
        entity.setImage(null); // Пока нет изображения
        return entity;
    }

    public User toUser(UserEntity userEntity) {
        if (userEntity == null) return null;

        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setPhone(userEntity.getPhone());
        user.setRole(userEntity.getRole());
        user.setImage(getUserImageUrl(userEntity));
        return user;
    }

    private String getUserImageUrl(UserEntity userEntity) {
        return userEntity.getImage() != null ?
               "/users/image/" + userEntity.getId() : null;
    }

    // === Ad mappings ===

    public AdEntity toAdEntity(CreateOrUpdateAd createOrUpdateAd) {
        if (createOrUpdateAd == null) return null;

        AdEntity entity = new AdEntity();
        entity.setTitle(createOrUpdateAd.getTitle());
        entity.setPrice(createOrUpdateAd.getPrice());
        entity.setDescription(createOrUpdateAd.getDescription());
        // author и image будут установлены позже
        return entity;
    }

    public Ad toAd(AdEntity adEntity) {
        if (adEntity == null) return null;

        Ad ad = new Ad();
        ad.setPk(adEntity.getId());
        ad.setAuthor(adEntity.getAuthor() != null ? adEntity.getAuthor().getId() : null);
        ad.setTitle(adEntity.getTitle());
        ad.setPrice(adEntity.getPrice());
        ad.setImage(getAdImageUrl(adEntity));
        return ad;
    }

    public ExtendedAd toExtendedAd(AdEntity adEntity) {
        if (adEntity == null) return null;

        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(adEntity.getId());
        extendedAd.setTitle(adEntity.getTitle());
        extendedAd.setPrice(adEntity.getPrice());
        extendedAd.setDescription(adEntity.getDescription());
        extendedAd.setImage(getAdImageUrl(adEntity));

        if (adEntity.getAuthor() != null) {
            extendedAd.setAuthorFirstName(adEntity.getAuthor().getFirstName());
            extendedAd.setAuthorLastName(adEntity.getAuthor().getLastName());
            extendedAd.setEmail(adEntity.getAuthor().getEmail());
            extendedAd.setPhone(adEntity.getAuthor().getPhone());
        }

        return extendedAd;
    }

    private String getAdImageUrl(AdEntity adEntity) {
        return adEntity.getImage() != null ?
               "/ads/image/" + adEntity.getId() : null;
    }

    // === Comment mappings ===

    public CommentEntity toCommentEntity(CreateOrUpdateComment createOrUpdateComment,
                                         UserEntity authorEntity,
                                         AdEntity adEntity) {
        if (createOrUpdateComment == null || authorEntity == null || adEntity == null) {
            return null;
        }

        CommentEntity entity = new CommentEntity();
        entity.setText(createOrUpdateComment.getText());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setAuthor(authorEntity);
        entity.setAd(adEntity);
        return entity;
    }

    public Comment toComment(CommentEntity comment) {
        if (comment == null) return null;

        Comment dto = new Comment();
        dto.setPk(comment.getId());
        dto.setText(comment.getText());

        // Конвертируем OffsetDateTime в миллисекунды
        if (comment.getCreatedAt() != null) {
            dto.setCreatedAt(comment.getCreatedAt().toInstant().toEpochMilli());
        }

        if (comment.getAuthor() != null) {
            dto.setAuthor(comment.getAuthor().getId());
            dto.setAuthorFirstName(comment.getAuthor().getFirstName());
            dto.setAuthorImage(getUserImageUrl(comment.getAuthor()));
        }

        return dto;
    }

    // === List mappings ===

    public List<Ad> toAdList(List<AdEntity> adEntities) {
        if (adEntities == null) return null;
        return adEntities.stream()
                .map(this::toAd)
                .collect(Collectors.toList());
    }

    public List<Comment> toCommentList(List<CommentEntity> commentEntities) {
        if (commentEntities == null) return null;
        return commentEntities.stream()
                .map(this::toComment)
                .collect(Collectors.toList());
    }
}
