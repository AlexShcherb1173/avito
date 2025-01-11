package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.service.AdsService;

@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdsController {

    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @GetMapping
    @Operation(summary = "Получение всех объявлений")
    public Ads getAllAds() {
        return adsService.getAllAds();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Добавление объявления")
    public Ads addAd(@RequestParam("image") MultipartFile image,
                     @RequestBody CreateOrUpdateAdDto ad,
                     Authentication authentication) {
        return adsService.addAds(image, ad, authentication);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    public Ads getUserAds(Authentication authentication) {
        return adsService.getUserAds(authentication);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.getAdById(#id).user.email.equals(authentication.name)")
    @Operation(summary = "Удаление объявления")
    public void removeAd(@PathVariable Integer id, Authentication authentication) {
        adsService.removeAd(id, authentication);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получение информации об объявлении")
    public ExtendedAdDto getAdById(@PathVariable Integer id) {
        return adsService.getAdById(id);
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.getAdById(#id).user.email.equals(authentication.name)")
    @Operation(summary = "Обновление информации об объявлении")
    public Ads updateAdById(@PathVariable Integer id,
                            @RequestBody CreateOrUpdateAdDto ad,
                            Authentication authentication) {
        return adsService.updateAdById(id, ad, authentication);
    }

    @PatchMapping("{id}/image")
    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.getAdById(#id).user.email.equals(authentication.name)")
    @Operation(summary = "Обновление картинки объявления")
    public String updateImageAd(@PathVariable Integer id,
                                @RequestParam("image") MultipartFile image,
                                Authentication authentication) {
        return adsService.updateImageAd(id, image, authentication);
    }
}