package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.entity.CommentEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.image", target = "authorImage")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "text", target = "text")
    Comment toDto(CommentEntity entity);

    List<Comment> toDtoList(List<CommentEntity> entities);

    default Long map(LocalDateTime value) {
        if (value == null) return null;
        return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}