package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.RoleDTO;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.utility.SecurityUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {

    private final AdMapper adMapper;
    private final AdRepository adRepository;
    private final UserServiceImpl userService;
    private final SecurityUtil securityUtil;
    private final ImageService imageService;

    public AdServiceImpl(AdMapper adMapper, AdRepository adRepository, UserServiceImpl userService, SecurityUtil securityUtil, ImageService imageService) {
        this.adMapper = adMapper;
        this.adRepository = adRepository;
        this.userService = userService;
        this.securityUtil = securityUtil;
        this.imageService = imageService;
    }

    @Override
    public AdDTO getAdDTO(Ad ad) {
        return adMapper.adToAdDTO(ad);
    }

    @Override
    public Ad createAdFromDTO(AdDTO adDTO) {
        return adMapper.adDTOToAd(adDTO);
    }

    @Override
    public AdDTO getAdById(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        return adMapper.adToAdDTO(ad);
    }

    @Override
    public AdDTO addAd(CreateOrUpdateAdDTO adDTO, MultipartFile image) {
        try {
            User author = userService.getCurrentUserEntity();
            String imageFileName = imageService.saveImage(image);

            Ad ad = new Ad()
                    .setTitle(adDTO.getTitle())
                    .setPrice(adDTO.getPrice())
                    .setDescription(adDTO.getDescription())
                    .setAuthor(author)
                    .setImage(imageFileName);

            ad = adRepository.save(ad);

            return adMapper.adToAdDTO(ad);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save ad image", e);
        }
    }

    @Override
    public void deleteAd(Integer id) {
        User currentUser = userService.getCurrentUserEntity();
        Ad ad = adRepository.findById(id).orElseThrow(() -> new RuntimeException("Ad not found"));
        if (currentUser.getRole() != RoleDTO.ADMIN && !ad.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new RuntimeException("You are not author of this ad");
        }
        adRepository.delete(ad);
    }

    @Override
    public AdsDTO getAllAds() {
        List<Ad> ads = adRepository.findAll();
        List<AdDTO> adDTOs = ads.stream()
                .map(adMapper::adToAdDTO)
                .collect(Collectors.toList());
        return new AdsDTO(adDTOs.size(), adDTOs);
    }

    @Override
    public AdDTO updateAd(Integer id, CreateOrUpdateAdDTO updateAd) {
        User currentUser = userService.getCurrentUserEntity();
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        if (currentUser.getRole() != RoleDTO.ADMIN && !ad.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new RuntimeException("You are not author of this ad");
        }
        ad.setTitle(updateAd.getTitle())
                .setPrice(updateAd.getPrice())
                .setDescription(updateAd.getDescription());
        adRepository.save(ad);
        return adMapper.adToAdDTO(ad);
    }

    @Override
    public AdsDTO getMyAds() {
        User user = userService.getCurrentUserEntity();
        List<Ad> ads = adRepository.findByAuthor(user);
        List<AdDTO> adDTOs = ads.stream()
                .map(adMapper::adToAdDTO)
                .collect(Collectors.toList());
        return new AdsDTO(adDTOs.size(), adDTOs);
    }

    @Override
    public boolean isAdAuthor(Integer adId, String username) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found"));
        return ad.getAuthor().getUsername().equals(username);
    }

    @Override
    public void updateAdImage(Integer id, MultipartFile image) {
        try {
            Ad ad = adRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ad not found"));
            String fileName = imageService.saveImage(image);
            ad.setImage(fileName);
            adRepository.save(ad);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
}
