package ru.skypro.homework.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateCommentDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;

import java.util.Objects;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

    /**
     * Маппинг комментария в объект CommentDto.
     * <p>
     * Аватар пользователя преобразуется из пути в файловой системе в URL-ссылку на изображение.
     * Если он равен null, в commentDto.authorImage также запишется null.
     */
    default CommentDto toDto(Comment comment) {

        if (comment == null) {
            return null;
        }

        CommentDto commentDto = new CommentDto();
        commentDto.setPk(comment.getCommentId());
        commentDto.setText(comment.getText());

        commentDto.setCreatedAt(Objects.requireNonNullElse(comment.getCreatedAt(), 0L));

        User author = comment.getAuthor();
        if (author != null) {

            commentDto.setAuthor(comment.getAuthor().getId());
            commentDto.setAuthorFirstName(comment.getAuthor().getFirstName());

            Image image = author.getImage();
            if (image != null) {
                commentDto.setAuthorImage("/image/" + comment.getAuthor().getImage().getId());
            }
        }

        return commentDto;
    }


    /**
     * Создание комментария с единственным заполненным полем text из объекта CreateCommentDto.
     */
    Comment toComment(CreateCommentDto createCommentDto);

    /**
     * Обновление поля text в комментарии данными из объекта CommentDto.
     */
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateComment(CommentDto commentDto, @MappingTarget Comment comment);

}