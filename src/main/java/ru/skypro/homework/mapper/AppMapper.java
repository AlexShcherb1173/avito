package ru.skypro.homework.mapper;

import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.*;

import java.io.IOException;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Instant.class, LocalDateTime.class, ZoneId.class})
public interface AppMapper {

    // === User Mappings ===
    @Mapping(source = "username", target = "email")
    Users registerToUser(Register register);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "role", target = "role")
    @Mapping(target = "image", expression = "java(user.getImage() != null ? \"/images/\" + user.getImage().getId() : null)")
    User userEntityToUserDTO(Users user);

    // === Ad Mappings ===
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "author", ignore = true)
    Ad createOrUpdateAdToAdEntity(CreateOrUpdateAd createOrUpdateAd);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "image", target = "image")
    AdDTO adEntityToAdDTO(Ad ad);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "author.email", target = "email")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "title", target = "title")
    ExtendedAd adEntityToExtendedAd(Ad ad);

    // === Comment Mappings ===
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.image", target = "authorImage")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(target = "createdAt", expression = "java(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())")
    @Mapping(source = "text", target = "text")
    CommentDTO commentEntityToCommentDTO(Comment comment);

    // Helper method for image path conversion
    default String convertImageToPath(Image image) {
        return image != null ? "/images/" + image.getId() : null;
    }

    Ad updateAd(CreateOrUpdateAd updateAd, Ad existingAd);

    Comment createOrUpdateCommentToCommentEntity(CreateOrUpdateComment createComment);
}