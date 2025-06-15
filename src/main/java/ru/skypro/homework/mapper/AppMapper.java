package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring", imports = {Instant.class, LocalDateTime.class, ZoneId.class})
public interface AppMapper {

    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "role", target = "role")
    UserEntity registerToUserEntity(Register register);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "image", target = "image")
    User userEntityToUser(UserEntity userEntity);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    UpdateUser userEntityToUpdateUser(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "author", source = "author")
    AdEntity createOrUpdateToAdEntity(CreateOrUpdateAd createOrUpdateAd, UserEntity author);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "title", target = "title")
    Ad adEntitytoAd(AdEntity adEntity);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.email", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    ExtendedAd adEntitytoExtendedAd(AdEntity adEntity);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.image", target = "authorImage")
    @Mapping(target = "createdAt", expression =
            "java(commentEntity.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())")
    @Mapping(source = "text", target = "text")
    Comment commentEntityToComment(CommentEntity commentEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createAt", target = "createdAt")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "adEntity", target = "ad")
    CommentEntity createOrUpdateCommentToCommentEntity(CreateOrUpdateComment comment,
                                                       AdEntity adEntity,
                                                       UserEntity author,
                                                       LocalDateTime createAt);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    void updateCommentEntityFromDto(CreateOrUpdateComment comment,
                                    @MappingTarget CommentEntity commentEntity);

    // это метод именно для обновления существующего объявления с помощью @MappingTarget
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateAdEntityFromDto(CreateOrUpdateAd createOrUpdateAd,
                               @MappingTarget AdEntity adEntity);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "title", target = "title")
    AdEntity createOrUpdateToAdEntity(Ad createOrUpdateAd);
}