package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdExtendedDto;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.impl.AdServiceImpl;

import javax.persistence.EntityNotFoundException;

@Service("CheckRoleService")
public class CheckRoleService {
    private final AdServiceImpl adService;
    private final CommentRepository commentRepository;

    public CheckRoleService(AdServiceImpl adService, CommentRepository commentRepository) {
        this.adService = adService;
        this.commentRepository = commentRepository;
    }

    public String getUserNameByAd(int id) {
        AdExtendedDto adExtendedDto = adService.getAdExtended(id);
        return adExtendedDto.getEmail();
    }

    public String getUserNameByComment(int id) {
        return commentRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Комментарий не найден"))
                .getUser().getUsername();

    }
}
