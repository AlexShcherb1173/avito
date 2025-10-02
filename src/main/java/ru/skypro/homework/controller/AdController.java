package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.model.Ad;

@RestController
@RequestMapping("/ads")
public class AdController {

    @Autowired
    private AdService adService;

    @PostMapping
    public Ad createAd(@RequestBody AdDto adDto, Authentication authentication) {
        Ad ad = new Ad();
        // Преобразование DTO в сущность
        ad.setTitle(adDto.getTitle());
        ad.setPrice(adDto.getPrice());
        ad.setDescription(adDto.getDescription());
        return adService.createAd(ad, authentication);
    }

    @PatchMapping("/{id}")
    public Ad updateAd(@PathVariable Integer id, @RequestBody AdDto adDto, Authentication authentication) {
        if (adService.canEditAd(id, authentication)) {
            Ad ad = new Ad();
            ad.setTitle(adDto.getTitle());
            ad.setPrice(adDto.getPrice());
            ad.setDescription(adDto.getDescription());
            return adService.createAd(ad, authentication);
        }
        throw new SecurityException("You are not authorized to edit this ad.");
    }

    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable Integer id, Authentication authentication) {
        if (adService.canEditAd(id, authentication)) {
            adService.deleteAd(id);
        } else {
            throw new SecurityException("You are not authorized to delete this ad.");
        }
    }
}
