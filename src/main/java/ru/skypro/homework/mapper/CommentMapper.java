package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorImage", expression = "java(comment.getAuthor().getImage())")
    @Mapping(target = "authorFirstName", expression = "java(comment.getAuthor().getFirstName())")
    @Mapping(target = "createdAt", source = "createdAt")
    CommentDTO commentToCommentDTO(Comment comment);

    default Integer mapAuthorId(User user) {
        return user != null ? user.getId() : null;
    }

    default String mapAuthorImage(User user) {
        return user != null ? user.getImage() : null;
    }

    default String mapAuthorFirstName(User user) {
        return user != null ? user.getFirstName() : null;
    }

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment commentDTOToComment(CommentDTO commentDTO);

    default Comment setCommentFields(Comment comment, User author, Ad ad, Long createdAt) {
        if (comment != null) {
            comment.setAuthor(author);
            comment.setAd(ad);
            comment.setCreatedAt(createdAt);
        }
        return comment;
    }
}