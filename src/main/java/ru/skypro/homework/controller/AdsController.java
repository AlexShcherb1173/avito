package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;

@RestController
@RequestMapping("/ads")
public class AdsController {

    @GetMapping
    public Ads getAllAds() {
        return new Ads();
    }

    @PostMapping
    public Ad createAd(@RequestBody CreateOrUpdateAd ad) {
        return new Ad();
    }

    @GetMapping("/{id}")
    public Ad getAd(@PathVariable Integer id) {
        return new Ad();
    }

    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable Integer id) {
    }

    @PatchMapping("/{id}")
    public Ad updateAd(@PathVariable Integer id, @RequestBody CreateOrUpdateAd ad) {
        return new Ad();
    }
}