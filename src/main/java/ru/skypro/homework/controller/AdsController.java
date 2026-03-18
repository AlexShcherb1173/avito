package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

/**
 * Контроллер для работы с объявлениями.
 *
 * Предоставляет API для получения, создания,
 * обновления и удаления объявлений,
 * а также для обновления изображений объявлений.
 */
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Объявления", description = "Операции с объявлениями")
public class AdsController {

    private final AdService adService;
    private final ImageService imageService;

    /**
     * Получить список всех объявлений.
     *
     * @return объект Ads со списком всех объявлений
     */
    @Operation(summary = "Получение всех объявлений")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    /**
     * Получить список объявлений текущего авторизованного пользователя.
     *
     * @param authentication данные аутентифицированного пользователя
     * @return объект Ads со списком объявлений пользователя
     */
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        return ResponseEntity.ok(adService.getAdsMe(authentication.getName()));
    }

    /**
     * Получить расширенную информацию об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return объект ExtendedAd, если объявление найдено,
     * иначе ответ 404 Not Found
     */
    @Operation(summary = "Получение объявления по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAdById(@PathVariable Integer id) {
        return adService.getAdById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Создать новое объявление с изображением.
     *
     * @param createOrUpdateAd данные объявления
     * @param file изображение объявления
     * @param authentication данные аутентифицированного пользователя
     * @return созданное объявление или 404, если автор не найден
     * @throws IOException если произошла ошибка при сохранении изображения
     */
    @Operation(summary = "Добавление нового объявления")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd createOrUpdateAd,
                                    @RequestPart("image") MultipartFile file,
                                    Authentication authentication) throws IOException {
        return adService.addAd(createOrUpdateAd, file, authentication.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Обновить данные объявления по его идентификатору.
     *
     * @param id идентификатор объявления
     * @param createOrUpdateAd новые данные объявления
     * @param authentication данные аутентифицированного пользователя
     * @return обновленное объявление или 404, если объявление не найдено
     */
    @Operation(summary = "Обновление объявления по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable Integer id,
                                       @RequestBody CreateOrUpdateAd createOrUpdateAd,
                                       Authentication authentication) {
        return adService.updateAd(id, createOrUpdateAd, authentication.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Удалить объявление по его идентификатору.
     *
     * @param id идентификатор объявления
     * @param authentication данные аутентифицированного пользователя
     * @return 204 No Content, если удаление успешно,
     * иначе 404 Not Found
     */
    @Operation(summary = "Удаление объявления по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Integer id,
                                         Authentication authentication) {
        if (adService.deleteAd(id, authentication.getName())) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Обновить изображение существующего объявления.
     *
     * @param id идентификатор объявления
     * @param image новый файл изображения
     * @param authentication данные аутентифицированного пользователя
     * @return обновленное объявление или 404, если объявление не найдено
     * @throws IOException если произошла ошибка при сохранении изображения
     */
    @PatchMapping("/{id}/image")
    public ResponseEntity<Ad> updateAdImage(@PathVariable Integer id,
                                            @RequestParam("image") MultipartFile image,
                                            Authentication authentication) throws IOException {
        String fileName = imageService.saveImage(image);

        return adService.updateAdImage(id, fileName, authentication.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}