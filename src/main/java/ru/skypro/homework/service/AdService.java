package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.entity.UserRole;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    public Ads getAllAds() {
        List<Ad> results = adRepository.findAll()
                .stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());

        Ads ads = new Ads();
        ads.setCount(results.size());
        ads.setResults(results);
        return ads;
    }

    public Ads getAdsByAuthorEmail(String email) {
        UserEntity author = getUserByEmailOrThrow(email);

        List<Ad> results = adRepository.findAllByAuthor_Id(author.getId())
                .stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());

        Ads ads = new Ads();
        ads.setCount(results.size());
        ads.setResults(results);
        return ads;
    }

    public ExtendedAd getExtendedAd(Integer adId) {
        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found"));
        return adMapper.toExtendedDto(ad);
    }

    public Ad addAd(String authorEmail, CreateOrUpdateAd createOrUpdateAd, String imagePath) {
        UserEntity author = getUserByEmailOrThrow(authorEmail);

        AdEntity entity = new AdEntity();
        entity.setAuthor(author);
        entity.setImage(imagePath);

        adMapper.applyCreateOrUpdate(createOrUpdateAd, entity);


        AdEntity saved = adRepository.save(entity);
        return adMapper.toAdDto(saved);
    }

    public Ad updateAd(Integer adId, String currentEmail, CreateOrUpdateAd createOrUpdateAd) {
        UserEntity currentUser = getUserByEmailOrThrow(currentEmail);

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found"));

        checkAdPermission(currentUser, ad);

        adMapper.applyCreateOrUpdate(createOrUpdateAd, ad);
        AdEntity saved = adRepository.save(ad);
        return adMapper.toAdDto(saved);
    }

    public void deleteAd(Integer adId, String currentEmail) {
        UserEntity currentUser = getUserByEmailOrThrow(currentEmail);

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found"));

        checkAdPermission(currentUser, ad);

        adRepository.delete(ad);
    }

    public byte[] updateAdImage(Integer adId, String currentEmail, String imagePath) {
        UserEntity currentUser = getUserByEmailOrThrow(currentEmail);

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found"));

        checkAdPermission(currentUser, ad);

        adRepository.save(ad);
        return new byte[0];
    }

    private UserEntity getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void checkAdPermission(UserEntity currentUser, AdEntity ad) {
        if (currentUser.getRole() == UserRole.ADMIN) {
            return;
        }
        Integer ownerId = ad.getAuthor() != null ? ad.getAuthor().getId() : null;
        if (ownerId == null || !ownerId.equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission for this ad");
        }
    }
}
