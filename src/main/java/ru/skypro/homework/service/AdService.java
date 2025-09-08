package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    public Ad createAd(CreateOrUpdateAd req, long authorId, String imagePath) {
        UserEntity author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + authorId));
        AdEntity entity = adMapper.fromCreateRequest(req, author, imagePath);
        entity = adRepository.save(entity);
        return adMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public ExtendedAd getExtended(long adId) {
        AdEntity entity = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Ad not found: " + adId));
        return adMapper.toExtendedDto(entity);
    }

    @Transactional(readOnly = true)
    public List<Ad> getByAuthor(long authorId) {
        UserEntity author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + authorId));
        return adRepository.findAllByAuthor(author).stream()
                .map(adMapper::toDto)
                .toList();
    }

    @PreAuthorize("@commentSecurity.isOwner(#commentId, authentication) or hasRole('ADMIN')")
    public Ad updateAd(long adId, CreateOrUpdateAd req, String imagePath) {
        AdEntity entity = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Ad not found: " + adId));
        adMapper.updateFrom(req, entity, imagePath);
        return adMapper.toDto(entity);
    }

    @PreAuthorize("@adSecurity.isOwner(#adId, authentication) or hasRole('ADMIN')")
    public void deleteAd(long adId) {
        adRepository.deleteById(adId);
    }
}
