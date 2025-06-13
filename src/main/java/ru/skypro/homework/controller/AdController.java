package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdService;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    @GetMapping

    public Ads getAllAds() {
        return adService.getAllAds();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public Ad addAd(@RequestPart(name = "properties") CreateOrUpdateAd createOrUpdateAd,
                    @RequestPart(name = "image") MultipartFile image)
    {
        return adService.addAd(createOrUpdateAd);
    }

    @GetMapping("/{id}")

    public ExtendedAd getAds(@PathVariable Integer id) {
        return adService.getAd(id);
    }

    @DeleteMapping("/{id}")

    public void removeAd(@PathVariable Integer id) {
        adService.deleteAd(id);
    }    @PatchMapping("/{id}")

      public Ad updateAds(@PathVariable Integer id,
           @RequestBody CreateOrUpdateAd createOrUpdateAd
    ) {
        return adService.updateAd(id, createOrUpdateAd);
    }

    @GetMapping("/me")
        public Ads getAdsMe(Authentication authentication) {
        return adService.getAdsMe();
    }

    @PatchMapping("/{id}/image")

    public void updateImage(
            @PathVariable Integer id,
            @RequestParam("image") MultipartFile image
    ) {
        adService.updateAdImage(id, image);
    }
}