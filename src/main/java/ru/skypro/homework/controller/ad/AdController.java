package ru.skypro.homework.controller.ad;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Объявления")
public class AdController {

    // Временная заглушка вместо реального сервиса
    @PostConstruct
    private void init() {
        log.warn("Используются заглушки вместо реального AdService!");
    }

    @GetMapping
    @Operation(summary = "Получение всех объявлений")
    public Ads getAllAds() {
        // Заглушка: возвращаем пустой список объявлений
        Ads stub = new Ads();
        stub.setCount(0);
        stub.setResults(Collections.emptyList());
        return stub;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавление объявления")
    public Ad addAd(
            @RequestPart CreateOrUpdateAd properties,
            @RequestPart MultipartFile image
    ) {
        // Заглушка: возвращаем тестовое объявление
        Ad stub = new Ad();
        stub.setPk(1);
        stub.setTitle(properties.getTitle());
        stub.setPrice(properties.getPrice());
        stub.setImage("http://example.com/stub-image.jpg");
        return stub;
    }

    @GetMapping("/{id}")
    public ExtendedAd getAd(@PathVariable Integer id) {
        ExtendedAd stub = new ExtendedAd();
        stub.setPk(id);
        stub.setTitle("Тестовое объявление");
        return stub;
    }
}
