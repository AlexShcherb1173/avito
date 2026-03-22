package ru.avito.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.avito.dto.ad.*;
import ru.avito.entity.Ad;
import ru.avito.entity.Role;
import ru.avito.entity.User;
import ru.avito.exception.ForbiddenException;
import ru.avito.exception.NotFoundException;
import ru.avito.mapper.AdMapper;
import ru.avito.repository.AdRepository;
import ru.avito.repository.UserRepository;
import ru.avito.security.SecurityUtils;
import ru.avito.service.AdService;
import ru.avito.service.ImageService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;

    @Override
    public AdsResponse getAllAds() {
        List<AdDto> ads = adRepository.findAll()
                .stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        return new AdsResponse(ads.size(), ads);
    }

    @Override
    public ExtendedAdDto getAdById(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ad not found"));

        return adMapper.toExtendedDto(ad);
    }

    @Override
    public AdDto createAd(CreateOrUpdateAdRequest request) {
        User user = getCurrentUser();

        Ad ad = Ad.builder()
                .title(request.getTitle())
                .price(request.getPrice())
                .description(request.getDescription())
                .author(user)
                .image(null)
                .build();

        Ad saved = adRepository.save(ad);
        return adMapper.toDto(saved);
    }

    @Override
    public AdDto updateAd(Integer id, CreateOrUpdateAdRequest request) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ad not found"));

        User currentUser = getCurrentUser();

        if (!isOwnerOrAdmin(currentUser, ad)) {
            throw new ForbiddenException("You cannot edit this ad");
        }

        ad.setTitle(request.getTitle());
        ad.setPrice(request.getPrice());
        ad.setDescription(request.getDescription());

        return adMapper.toDto(adRepository.save(ad));
    }

    @Override
    public void deleteAd(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ad not found"));

        User currentUser = getCurrentUser();

        if (!isOwnerOrAdmin(currentUser, ad)) {
            throw new ForbiddenException("You cannot delete this ad");
        }

        imageService.deleteImageIfExists(ad.getImage());
        adRepository.delete(ad);
    }

    @Override
    public AdsResponse getMyAds() {
        User user = getCurrentUser();

        List<AdDto> ads = adRepository.findAllByAuthorId(user.getId())
                .stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        return new AdsResponse(ads.size(), ads);
    }

    @Override
    public ImageResponse updateAdImage(Integer id, MultipartFile image) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ad not found"));

        User currentUser = getCurrentUser();

        if (!isOwnerOrAdmin(currentUser, ad)) {
            throw new ForbiddenException("You cannot update image for this ad");
        }

        imageService.deleteImageIfExists(ad.getImage());

        String imageUrl = imageService.saveAdImage(ad.getId(), image);
        ad.setImage(imageUrl);
        adRepository.save(ad);

        return new ImageResponse(imageUrl);
    }

    private User getCurrentUser() {
        String email = SecurityUtils.getCurrentUsername();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private boolean isOwnerOrAdmin(User user, Ad ad) {
        return ad.getAuthor().getId().equals(user.getId())
                || user.getRole() == Role.ADMIN;
    }
}