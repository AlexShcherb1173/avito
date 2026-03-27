package ru.avito.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.avito.dto.ad.AdDto;
import ru.avito.dto.ad.AdsResponse;
import ru.avito.dto.ad.CreateOrUpdateAdRequest;
import ru.avito.dto.ad.ExtendedAdDto;
import ru.avito.dto.ad.ImageResponse;
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
    @Transactional(readOnly = true)
    public AdsResponse getAllAds() {
        List<AdDto> ads = adRepository.findAll()
                .stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        return new AdsResponse(ads.size(), ads);
    }

    @Override
    @Transactional(readOnly = true)
    public ExtendedAdDto getAdById(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ad not found"));

        return adMapper.toExtendedDto(ad);
    }

    @Override
    @Transactional
    public AdDto createAd(CreateOrUpdateAdRequest request, MultipartFile image) {
        validateImage(image);

        User currentUser = getCurrentUser();

        Ad ad = Ad.builder()
                .title(request.getTitle())
                .price(request.getPrice())
                .description(request.getDescription())
                .author(currentUser)
                .image(null)
                .build();

        Ad savedAd = adRepository.save(ad);

        String imageUrl = imageService.saveAdImage(savedAd.getId(), image);
        savedAd.setImage(imageUrl);

        Ad updatedAd = adRepository.save(savedAd);
        return adMapper.toDto(updatedAd);
    }

    @Override
    @Transactional
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

        Ad updatedAd = adRepository.save(ad);
        return adMapper.toDto(updatedAd);
    }

    @Override
    @Transactional
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
    @Transactional(readOnly = true)
    public AdsResponse getMyAds() {
        User currentUser = getCurrentUser();

        List<AdDto> ads = adRepository.findAllByAuthorId(currentUser.getId())
                .stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        return new AdsResponse(ads.size(), ads);
    }

    @Override
    @Transactional
    public ImageResponse updateAdImage(Integer id, MultipartFile image) {
        validateImage(image);

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

        if (email == null || email.isBlank()) {
            throw new ForbiddenException("User is not authenticated");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));
    }

    private boolean isOwnerOrAdmin(User user, Ad ad) {
        return ad.getAuthor().getId().equals(user.getId())
                || user.getRole() == Role.ADMIN;
    }

    private void validateImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image is required");
        }

        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
    }
}