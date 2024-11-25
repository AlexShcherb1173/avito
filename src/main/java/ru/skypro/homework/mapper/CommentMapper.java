package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.Comment;

/**
 * Интерфейс CommentMapper отвечает за преобразование объектов между
 * сущностями Comment и CommentDTO с использованием MapStruct.
 *
 * <p>Данный маппер позволяет автоматически преобразовывать данные между
 * слоями приложения, упрощая работу с объектами и уменьшая объем
 * повторяющегося кода.</p>
 *
 * <p>Методы:</p>
 * <ul>
 *     <li>{@link #commentDtoToComment(CommentDTO)} - преобразует объект
 *     CommentDTO в объект Comment.</li>
 *     <li>{@link #commentToCommentDto(Comment)} - преобразует объект
 *     Comment в объект CommentDTO.</li>
 *     <li>{@link #avatarToString(Avatar)} - преобразует объект Avatar в строку,
 *     представляющую URL изображения аватара.</li>
 * </ul>
 *
 * <p>Примечания:</p>
 * <ul>
 *     <li>Поле {@code author} в объекте Comment игнорируется при
 *     преобразовании из CommentDTO.</li>
 *     <li>Поле {@code createdAt} также игнорируется при преобразовании
 *     из CommentDTO.</li>
 *     <li>При преобразовании из Comment в CommentDTO используется поле
 *     {@code author.id} для установки автора, а также поле
 *     {@code author.avatar} для получения изображения аватара.</li>
 * </ul>
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {
    String address = "/users/image/";

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "id", source = "pk")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "listing", ignore = true)
    Comment commentDtoToComment(CommentDTO dto);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(source = "id", target = "pk")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "authorImage", source = "author.avatar", qualifiedByName = "avatarToString")
    CommentDTO commentToCommentDto(Comment entity);

    @Named("avatarToString")
    default String avatarToString(Avatar avatar) {
        if (avatar == null) {
            return null;
        }
        return address + avatar.getId();
    }
}
