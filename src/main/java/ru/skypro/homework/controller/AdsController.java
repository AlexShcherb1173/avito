package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.service.AdService;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdService adService;

    @GetMapping
    public AdsDto getAllAds() {
        return adService.getAllAds();
    }

    @GetMapping("/{id}")
    public AdDto getAd(@PathVariable Integer id) {
        return adService.getAdById(id);
    }

    @PostMapping
    public AdDto createAd(Authentication authentication, @RequestBody CreateOrUpdateAdDto ad) {
        return adService.createAd(authentication, ad);
    }

    @DeleteMapping("/{id}")
    public void deleteAd(Authentication authentication, @PathVariable Integer id) {
        adService.deleteAd(authentication, id);
    }

    @PatchMapping("/{id}")
    public AdDto updateAd(Authentication authentication, @PathVariable Integer id, @RequestBody CreateOrUpdateAdDto ad) {
        return adService.updateAd(authentication, id, ad);
    }
}