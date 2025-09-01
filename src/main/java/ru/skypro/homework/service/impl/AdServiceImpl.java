package ru.skypro.homework.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.responseDto.AdDto;
import ru.skypro.homework.responseDto.AdsResponse;
import ru.skypro.homework.responseDto.CommentDto;
import ru.skypro.homework.responseDto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final ImageService imageService;

    @Override
    public AdDto createAd(User user, CreateOrUpdateAd dto, MultipartFile image) {
        // TODO: реализовать
        return null;
    }

    @Override
    public ExtendedAdDto getExtendedAd(Long id) {
        // TODO: реализовать
        return null;
    }

    @Override
    public AdDto updateAd(Long id, CreateOrUpdateAd dto) {
        // TODO: реализовать
        return null;
    }

    @Override
    public void deleteAd(Long id) {
        // TODO: реализовать
    }

    @Override
    public String updateAdImage(Long id, MultipartFile image) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));

        // Опционально: удалить старое изображение
        // String oldImage = ad.getImage();
        // if (oldImage != null) { ... delete ... }

        String filename = imageService.saveImage(image, "ads"); // ✅ возвращает имя
        String imageUrl = "/images/ads/" + filename;
        ad.setImage(imageUrl);
        adRepository.save(ad);

        return imageUrl; // ✅ возвращаем URL
    }

    @Override
    public AdsResponse getMyAds (User user) {
        List<Ad> ads = adRepository.findByAuthor(user);
        List<AdDto> dtos = ads.stream()
                .map(this::convertToAdDto)
                .collect(Collectors.toList());
        return new AdsResponse(dtos.size(), dtos);
    }

    private AdDto convertToAdDto(Ad ad) {
        AdDto dto = new AdDto();
        dto.setPk(Math.toIntExact(ad.getId()));
        dto.setAuthor(Math.toIntExact(ad.getAuthor().getId()));
        dto.setTitle(ad.getTitle());
        dto.setPrice(ad.getPrice());
        dto.setImage(ad.getImage()); // например: "/images/ads/1.jpg"
        return dto;
    }

    @Override
    public AdsResponse getAllAds() {
        List<Ad> ads = adRepository.findAll();
        List<AdDto> dtos = ads.stream()
                .map(this::convertToAdDto)
                .collect(Collectors.toList());
        return new AdsResponse(dtos.size(), dtos);
    }
}