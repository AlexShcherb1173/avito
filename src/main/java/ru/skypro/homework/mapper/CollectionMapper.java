package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CollectionMapper {
    private final AdMapper adMapper;
    private final CommentMapper commentMapper;

    //для списка объявлений
    public List<AdDto> adsToDto(List<AdEntity> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());
    }

    // для списка комментариев
    public List<CommentDto> commentsToDto(List<CommentEntity> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    //для пагинации и т.д.
    public AdsDto toAdsDto(List<AdEntity> entities) {
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(entities.size());
        adsDto.setResults(adsToDto(entities));
        return adsDto;
    }

    public CommentsDto toCommentsDto(List<CommentEntity> entities) {
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setCount(entities.size());
        commentsDto.setResults(commentsToDto(entities));
        return commentsDto;
    }
}

