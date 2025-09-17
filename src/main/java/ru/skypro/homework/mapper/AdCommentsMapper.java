package ru.skypro.homework.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = CommentMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AdCommentsMapper {

    @Mapping(
            target = "count",
            expression = "java(ad.getComments() == null ? 0 : ad.getComments().size())"
    )
    @Mapping(target = "results", source = "comments")
    CommentsDto toCommentsDto(Ad ad);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<CommentsDto> toCommentDtos(List<Comment> comments);
}
