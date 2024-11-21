package ru.skypro.homework.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdService;

@RestController
@RequestMapping("/ads")
public class AdController {

    @Autowired
    private AdService adService; // Сервис для работы с объявлениями

    @GetMapping
    public Ads getAllAds() {
        // Логика получения всех объявлений
        return adService.getAllAds();
    }

    @PostMapping
    public Ad addAd(@RequestParam("image") MultipartFile image, @RequestBody CreateOrUpdateAd ad) {
        // Логика добавления объявления
        return adService.addAd(image, ad);
    }

    @GetMapping("/{id}")
    public ExtendedAd getAdById(@PathVariable Integer id) {
        // Логика получения объявления по ID
        return adService.getAdById(id);
    }

    @PatchMapping("/{id}")
    public String updateAd(@PathVariable Integer id, @RequestBody CreateOrUpdateAd ad) {
        // Логика обновления объявления
        adService.updateAd(id, ad);
        return "Объявление успешно обновлено";
    }

    @DeleteMapping("/{id}")
    public String removeAd(@PathVariable Integer id) {
        // Логика удаления объявления
        adService.removeAd(id);
        return "Объявление успешно удалено";
    }
}