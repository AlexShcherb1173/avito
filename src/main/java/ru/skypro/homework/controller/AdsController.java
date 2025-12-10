package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;

import javax.validation.Valid;

/**
 * REST контроллер для управления объявлениями.
 * Обрабатывает HTTP запросы связанные с созданием, получением, обновлением и удалением объявлений.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;

    /**
     * Получает список всех объявлений.
     *
     * @return ResponseEntity с объектом {@link Ads} и статусом 200 OK
     */
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        log.info("Getting all ads");
        Ads ads = adsService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    /**
     * Создает новое объявление.
     * Требуется аутентификация пользователя.
     *
     * @param properties данные объявления
     * @param image изображение объявления
     * @param authentication объект аутентификации Spring Security
     * @return ResponseEntity с созданным объявлением и статусом 201 Created,
     *         или 401 Unauthorized при ошибке аутентификации
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(@RequestPart("properties") @Valid CreateOrUpdateAd properties,
                                    @RequestPart("image") MultipartFile image,
                                    Authentication authentication) {
        log.info("Adding new ad by user: {}", authentication.getName());
        try {
            Ad ad = adsService.addAd(properties, image, authentication.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(ad);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Получает расширенную информацию об объявлении по идентификатору.
     *
     * @param id идентификатор объявления
     * @return ResponseEntity с объектом {@link ExtendedAd} и статусом 200 OK,
     *         или 404 Not Found если объявление не существует
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {
        log.info("Getting extended ad with id: {}", id);
        try {
            ExtendedAd extendedAd = adsService.getExtendedAd(id);
            return ResponseEntity.ok(extendedAd);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Удаляет объявление по идентификатору.
     * Доступно только администраторам или владельцам объявления.
     *
     * @param id идентификатор объявления
     * @param authentication объект аутентификации Spring Security
     * @return ResponseEntity со статусом 204 No Content при успешном удалении,
     *         403 Forbidden при недостаточных правах,
     *         401 Unauthorized при ошибке аутентификации
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable Integer id, Authentication authentication) {
        log.info("Removing ad with id: {} by user: {}", id, authentication.getName());
        try {
            adsService.removeAd(id, authentication.getName());
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Обновляет информацию об объявлении.
     * Доступно только администраторам или владельцам объявления.
     *
     * @param id идентификатор объявления
     * @param updateAd обновленные данные объявления
     * @param authentication объект аутентификации Spring Security
     * @return ResponseEntity с обновленным объявлением и статусом 200 OK,
     *         403 Forbidden при недостаточных правах,
     *         401 Unauthorized при ошибке аутентификации
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable Integer id,
                                        @RequestBody @Valid CreateOrUpdateAd updateAd,
                                        Authentication authentication) {
        log.info("Updating ad with id: {} by user: {}", id, authentication.getName());
        try {
            Ad ad = adsService.updateAd(id, updateAd, authentication.getName());
            return ResponseEntity.ok(ad);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Получает список объявлений текущего аутентифицированного пользователя.
     *
     * @param authentication объект аутентификации Spring Security
     * @return ResponseEntity с объектом {@link Ads} и статусом 200 OK,
     *         или 401 Unauthorized при ошибке аутентификации
     */
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        log.info("Getting ads for user: {}", authentication.getName());
        try {
            Ads ads = adsService.getAdsByUser(authentication.getName());
            return ResponseEntity.ok(ads);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Обновляет изображение объявления.
     * Доступно только администраторам или владельцам объявления.
     *
     * @param id идентификатор объявления
     * @param image новое изображение
     * @param authentication объект аутентификации Spring Security
     * @return ResponseEntity со статусом 200 OK при успешном обновлении,
     *         403 Forbidden при недостаточных правах,
     *         401 Unauthorized при ошибке аутентификации
     */
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable Integer id,
                                         @RequestParam("image") MultipartFile image,
                                         Authentication authentication) {
        log.info("Updating image for ad with id: {} by user: {}", id, authentication.getName());
        try {
            adsService.updateAdImage(id, image, authentication.getName());
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}