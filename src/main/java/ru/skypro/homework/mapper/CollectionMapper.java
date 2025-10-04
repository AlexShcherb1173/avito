package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.comments.CommentDto;
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

    //для пагинации
}
