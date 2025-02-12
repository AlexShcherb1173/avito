package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("ads")
@CrossOrigin(value = "http://localhost:3000")
@Validated
@Slf4j
@Tag(name = "Объявления")
public class AdController {
    private final AdService adService;
    private final UserRepository userRepository;

    @Autowired
    public AdController(AdService adService, UserRepository userRepository) {
        this.adService = adService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Получение всех объявлений")
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Operation(summary = "Добавление объявления")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(@RequestPart(name = "properties") CreateOrUpdateAd properties,
                                    @RequestPart(name = "image") MultipartFile image,
                                    Principal principal) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(adService.createAd(properties, image, principal.getName()));
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Principal principal) {
        return ResponseEntity.ok(adService.getAdsByUser(principal.getName()));
    }

    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable int id) {
        boolean removed = adService.removeAd(id);
        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Обновление информации об объявлении")
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable int id,
                                       @RequestBody CreateOrUpdateAd properties) {
        Ad updatedAd = adService.updateAd(id, properties);
        if (updatedAd == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedAd);
    }

    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable int id) {
        return ResponseEntity.ok(adService.getAd(id));
    }

    @Operation(summary = "Обновление картинки объявления")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateImage(@PathVariable int id,
                                            @RequestPart("image") MultipartFile image) throws IOException {
        boolean updated = adService.updateImage(id, image);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
