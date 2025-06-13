package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface AppMapper {

    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "role", target = "role")
    UserEntity registerToUserEntity(Register register);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    AdEntity createOrUpdateToAdEntity(Ad createOrUpdateAd);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "title", target = "title")
    Ad adEntitytoAd(AdEntity adEntity);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "author.email", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    ExtendedAd adEntitytoExtendedAd(AdEntity adEntity);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.image", target = "authorImage")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "text",target = "text")
    Comment commentEntityToComment(CommentEntity commentEntity);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "authorFirstName", target = "author.firstName")
    @Mapping(source = "authorImage", target = "author.image")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "author", target = "author.id")
    @Mapping(source = "text",target = "text")
    Comment CommentToCommentEntity(CommentEntity commentEntity);

}
