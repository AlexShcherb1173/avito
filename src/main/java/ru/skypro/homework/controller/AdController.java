package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.service.AdService;



import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("ads")
public class AdController {

    private final AdService adService;


    @GetMapping
    // Получение всех объявлений
    public ResponseEntity<AdsDTO> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // Добавление объявления. required: image, properties, status - 201 Created или 401 unauthorized
    // Примечание: не уверен в корректности authentication, надо будет проверить
    public ResponseEntity<AdDTO> addAd (
            @RequestPart MultipartFile image,
            @RequestPart CreateOrUpdateAdDTO properties,
            Authentication authentication) {
        if (authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(adService.addAd(image, properties, authentication.getName()));
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("{id}")
    // Получение информации об объявлении (возможные ответы 200, 401, 404)
    public ResponseEntity<ExtendedAdDTO> getAds(@PathVariable int id, Authentication authentication) {
        if (authentication.isAuthenticated()) {
            if (adService.getAds(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else {
                return ResponseEntity.ok(adService.getAds(id));
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.findAdById(id).author.email.equals(authentication.name)")
    @DeleteMapping("{id}")
    // Удаление объявления (возможные ответы 204 No Content, 401 Unauthorized, 403 Forbidden, 404 Not found)
    public ResponseEntity<Optional> removeAd(@PathVariable int id, Authentication authentication) {
        if (authentication.isAuthenticated()) {
            if (adService.getAds(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else {
                adService.removeAd(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            // а что проверяем на 403 ошибку?? права пользователя?
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or adServiceImpl.findAdById(id).author.email.equals(authentication.name)")
    @PatchMapping("{id}")
    // Обновление информации об объявлении
    public ResponseEntity<AdDTO> updateAds(
            @PathVariable int id,
            @RequestBody CreateOrUpdateAdDTO properties,
            Authentication authentication) {
        if (authentication.isAuthenticated()) {
            if (adService.getAds(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else {
                return ResponseEntity.ok(adService.updateAds(id, properties));
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("me")
    // Получение объявлений авторизованного пользователя (200 или 401)
    public ResponseEntity<AdsDTO> getAdsMe(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(adService.getAdsMe());
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PreAuthorize("hasRole('ADMIN') or adServiceImpl.findAdById(id).author.email.equals(authentication.name)")
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // Обновление картинки объявления (статусы 200, 403, 401, 404)
    public ResponseEntity<String[]> updateImage (
            @PathVariable int id,
            @RequestParam MultipartFile image,
            Authentication authentication) {
        if (authentication.isAuthenticated()) {
            if (adService.getAds(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else {
                return ResponseEntity.ok(adService.updateImage(id, image));
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
