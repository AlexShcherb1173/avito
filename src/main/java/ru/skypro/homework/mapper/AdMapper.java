package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.repository.UserRepository;
@Component
public class AdMapper {
    private UserRepository userRepository;


    public AdMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public AdDto adToDto(Ad ad) {
        AdDto adToDto = new AdDto();
        adToDto.setPk(ad.getPk());
        adToDto.setTitle(ad.getTitle());
        adToDto.setPrice(ad.getPrice());
        adToDto.setImage(ad.getImage());
        return adToDto;
    }

    public Ad dtoToAd(AdDto adDto) {
        Ad ad = new Ad();
        ad.setPk(adDto.getPk());
        ad.setTitle(adDto.getTitle());
        ad.setPrice(adDto.getPrice());
        ad.setUser(userRepository.findById(adDto.getAuthor()).orElseThrow());
        ad.setPk(adDto.getPk());
        return ad;
    }
}
