package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.io.IOException;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@Tag(name = "Объявления")
@RequiredArgsConstructor
public class AdController {

    private final AdServiceImpl adServiceImpl;

    @Operation(summary = "Получение всех объявлений", tags = {"Объявления"})
    @GetMapping(path = "/ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class))
            })
    })
    public ResponseEntity<Ads> getAllAds(Integer id) {
        log.info("Метод getAllAds, класса AdController");
        return new ResponseEntity<>(adServiceImpl.getAllAds(), HttpStatus.OK);
    }

    @Operation(summary = "Добавление объявления", tags = {"Объявления"})
    @PostMapping(path = "/ads", consumes = "multipart/from-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = ""))
    })
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                                    @RequestPart(value = "imagine", required = true) MultipartFile image,
                                    Authentication authentication) throws IOException {
        log.info("Метод addAds, класса AdController. Приняты: " +
                        "Новое объявление {}. Изображение объявления{}",
                        properties.toString(), image.getOriginalFilename());
        Ad ad = adServiceImpl.addAd(properties, image, authentication.getName());
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(ad, HttpStatus.CREATED);
        }
    }

    @Operation(summary = "Получение информации об объявлении", tags = {"Объявления"})
    @GetMapping(path = "/ads/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class)),
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> getAds(@PathVariable Integer id) {
        log.info("Метод getAdsById, класса AdController. Принят: \n(int) id {}", id);
        if (adServiceImpl.existsById(id)) { // Проверка есть ли объявление или нет
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого объявления не существует");
        }
        AdEntity ad = adServiceImpl.getAds(id);
        return ResponseEntity.ok(ad);
    }

    @Operation(summary = "Удаление объявления", tags = {"Объявления"})
    @DeleteMapping(path = "/ads/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> removeAd(@PathVariable("id") Integer id) {
        log.info("Метод deleteAdsById, класса AdController. Принят: \n(int) id {}", id);
        if (adServiceImpl.existsById(id)) { // Проверка есть ли объявление или нет
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого объявления не существует");
        }
        adServiceImpl.removeAd(id);
        return ResponseEntity.ok().body("Объявление успешно удалено");
    }

    @Operation(summary = "Обновление информации в объявлении", tags = {"Объявления"})
    @PatchMapping(path = "/ads/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> updateAd(@PathVariable Integer id, @RequestBody AdEntity adModel) {
        log.info("Метод addAds, класса AdController. Приняты: \n(int) id {}\nНовое объявление или обновление имеющегося {}",
                id, adModel.toString());
        if (adServiceImpl.existsById(id)) { // Проверка есть ли объявление или нет
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого объявления не существует");
        }
        try {
            adServiceImpl.updateAd(id, adModel);
            return ResponseEntity.ok().body("Объявление успешно обновлено");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя", tags = {"Объявления"})
    @GetMapping(path = "/ads/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> getAdsMe(@PathVariable Integer id) {
        log.info("Метод getAdsCurrentUser, класса AdController.");
        if (adServiceImpl.existsById(id)) { // Проверка есть ли объявление или нет
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого объявления не существует");
        }
        adServiceImpl.getAdsMe(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление картинки объявления", tags = {"Объявления"})
    @PatchMapping(path = "/ads/{id}/image", consumes = "multipart/from-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/octet-stream",
                            array = @ArraySchema(schema = @Schema(type = "string", format = "byte")))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> updateImage(@PathVariable("id") Integer id,
                                         @RequestPart(value = "image", required = true) MultipartFile image,
                                         @RequestPart String username) {
        log.info("Метод addAds, класса AdController. Приняты: \n(int) id {}\nИзображение объявления{}",
                id, image.getOriginalFilename());
        if (!adServiceImpl.existsById(id)) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого объявления не существует");
        }
        adServiceImpl.updateImage(id, image, username);
        return ResponseEntity.ok().body("Изображение успешно обновлено");
    }
}
