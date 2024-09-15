package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.generatedDto.CommentDto;
import ru.skypro.homework.dto.generatedDto.CommentsDto;

public interface CommentService {

    CommentDto createNewComment(Long adId, CommentDto commentDto, Authentication authentication);
    CommentsDto getAllCommentsForAd(Long adsId);
    void deleteComment(long adPk, long id,  Authentication authentication);
    CommentDto updateComment(long adPk, long id, CommentDto commentDto, Authentication authentication);

}
