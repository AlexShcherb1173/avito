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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
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
    @PostMapping(path = "/ads", consumes = "multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                                    @RequestPart(value = "image", required = true) MultipartFile image,
                                    Authentication authentication) throws IOException {
        log.info("Метод addAd, класса AdController. Приняты: \nНовое объявление или обновление имеющегося {}" +
                "\nИзображение объявления{}", properties.toString(), image.getOriginalFilename());
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
                            schema = @Schema(implementation = ExtendedAd.class)),
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id, Authentication authentication) {
        log.info("Метод getAdsById, класса AdController. Принят: (int) id {}", id);
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adServiceImpl.existId(id)) {
            return ResponseEntity.ok(adServiceImpl.getAdById(id));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Удаление объявления", tags = {"Объявления"})
    @DeleteMapping(path = "/ads/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> removeAd(@PathVariable("id") Integer id, Authentication authentication) {
        log.info("Метод removeAdsById, класса AdController. Принят: (int) id {}", id);
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adServiceImpl.existId(id)) {
            adServiceImpl.deleteAdById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<Ad> updateAd(@PathVariable Integer id, @RequestBody CreateOrUpdateAd createAd,
                                       Authentication authentication) {
        log.info("Метод updateAd, класса AdController. Приняты: (int) id объявления: {}." +
                "Новое объявление или обновление имеющегося {}", id,
                createAd.toString());
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adServiceImpl.existId(id)) {
            Ad ad = adServiceImpl.updateInfoAboutAd(id, createAd);
            log.info("Получен объект Ad обратно в контроллер: {}", ad);
            return new ResponseEntity<>(ad, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        log.info("Метод getAdsCurrentUser, класса AdController.");
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return ResponseEntity.ok(adServiceImpl.getAds(authentication));
        }
    }

    @Operation(summary = "Обновление картинки объявления", tags = {"Объявления"})
    @PatchMapping(path = "/ads/{id}/image", consumes = "multipart/from-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/octet-stream",
                            array = @ArraySchema(schema = @Schema(type = "string", format = "byte")))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<byte[]> updateImage(@PathVariable("id") Integer id,
                                         @RequestPart(value = "image", required = true) MultipartFile image,
                                         Authentication authentication) throws IOException {
        log.info("Метод addAds, класса AdController. Приняты: (int) id {}. Изображение объявления{}",
                id, image.getOriginalFilename());
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adServiceImpl.existId(id)) {
            byte[] imageArrayBytes = adServiceImpl.updateImageAd(id, image);
            log.info("Получен массив байт в контроллер: {}", imageArrayBytes[0]);
            return new ResponseEntity<>(imageArrayBytes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/ad_images/{fileName}",
            produces = {MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE, "image/*"})
    public ResponseEntity<byte[]> getAdImageByFilename(@PathVariable String fileName) throws IOException {
        log.info("Вошли в метод getAdImageByFilename, класса UserController.");
        byte[] adImageData = adServiceImpl.findAdImageByFilename(fileName);
        log.info("Получен массив байт (выведем первый байт): {}", adImageData[0]);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.IMAGE_GIF)
                .body(adImageData);
    }
}
