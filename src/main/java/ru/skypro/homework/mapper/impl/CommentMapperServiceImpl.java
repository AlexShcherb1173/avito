package ru.skypro.homework.mapper.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapperService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

@Service
public class CommentMapperServiceImpl implements CommentMapperService {

    @Value(value = "${ads.image}")
    private String linkImage;

    @Override
    public CommentDto mappingToDto(CommentEntity entity) {
        CommentDto commentDto = new CommentDto();
        UserEntity userEntity = entity.getUser();
        commentDto.setAuthor(userEntity.getId());
        ImageEntity imageEntity = userEntity.getImageEntity();
        if (imageEntity != null) {
            commentDto.setAuthorImage(String.format(linkImage, imageEntity.getId()));
        }
        commentDto.setAuthorFirstName(userEntity.getFirstName());
        LocalDateTime date = entity.getCreatedAt();
        ZonedDateTime zdt = date.atZone(ZoneId.of(TimeZone.getDefault().getID()));
        long createAt = zdt.toInstant().toEpochMilli();;
        commentDto.setCreatedAt(createAt);
        commentDto.setPk(entity.getId());
        commentDto.setText(entity.getText());
        return commentDto;
    }

    @Override
    public CommentEntity mappingToEntity(CommentDto dto) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(dto.getPk());
        commentEntity.setText(dto.getText());
        commentEntity.setCreatedAt(LocalDateTime.ofEpochSecond(dto.getCreatedAt(), 0, ZoneOffset.UTC));
        return commentEntity;
    }
}
