package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.dto.AdDto;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;

    public AdDto getAdById(Integer id) {
        Ad ad = adRepository.findById(id).orElseThrow();
        return new AdDto(ad.getPk(), ad.getTitle(), ad.getPrice(), ad.getDescription(), ad.getAuthor().getId(), ad.getImage());
    }

    public AdDto createAd(AdDto adDto) {
        Ad ad = new Ad();
        ad.setTitle(adDto.getTitle());
        ad.setPrice(adDto.getPrice());
        ad.setDescription(adDto.getDescription());
        ad.setImage(adDto.getImage());
        ad.setAuthor(new User()); // Привязка к пользователю
        adRepository.save(ad);
        return new AdDto(ad.getPk(), ad.getTitle(), ad.getPrice(), ad.getDescription(), ad.getAuthor().getId(), ad.getImage());
    }
}
