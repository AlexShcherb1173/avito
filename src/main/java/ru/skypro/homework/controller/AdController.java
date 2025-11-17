package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.impl.AdServiceImpl;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "Методы для работы с рекламными объявлениями")
public class AdController implements AdControllerInterface {
    private final AdServiceImpl adServiceImpl;

    public AdController(AdServiceImpl adServiceImpl) {
        this.adServiceImpl = adServiceImpl;
    }

    @GetMapping
    @Override
    public AdsDto getAllAds() {
        return adServiceImpl.getAllAds();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    @Override
    public AdDto addAd(@RequestPart("properties") @Valid CreateOrUpdateAdDto properties, @RequestPart("image") MultipartFile image) throws IOException {
        return adServiceImpl.addAd(properties, image);
    }

    @GetMapping("/{id}")
    @Override
    public ExtendedAdDto getAds(@PathVariable(name = "id") Integer id) {
        return adServiceImpl.getAdById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.findAdById(#id).user.email == authentication.name")
    @Override
    public void removeAd(@PathVariable(name = "id") Integer id) {
        adServiceImpl.removeAd(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.findAdById(#id).user.email == authentication.name")
    @Override
    public AdDto updateAds(@PathVariable(name = "id") Integer id, @RequestBody CreateOrUpdateAdDto createOrUpdateAdDto) {
        return adServiceImpl.updateAd(id, createOrUpdateAdDto);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Override
    public AdsDto getAdsMe() {
        return adServiceImpl.getAllMyAds();
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@adServiceImpl.findAdById(#id).user.email == authentication.name")
    @Override
    public byte[] updateImage(@PathVariable(name = "id") Integer id, @RequestParam("image") MultipartFile image) throws IOException {
        return adServiceImpl.uploadAdPicture(id, image);
    }
}
