package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdvertsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.service.AdvertService;

/**
 * Контроллер для работы с объявлениями
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "Эндпойнты для работы с объявлениями")
@RequiredArgsConstructor
public class AdvertController {

    private final AdvertService service;

    @GetMapping
    @Operation(summary = "Получение всех объявлений",
            operationId = "getAllAds")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<?> getAllAdverts() {
        return null;
    }

    @PostMapping
    @Operation(summary = "Добавление объявления",
            operationId = "addAd")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<?> createA(@RequestPart CreateOrUpdateAdDto advert,
                                     @RequestPart(name = "image") MultipartFile file) {
        return new ResponseEntity<>(service.create(advert, file), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение комментариев объявления",
            operationId = "getAdverts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> getInfoAdverts(@PathVariable Integer id) {
        return null;
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удаление объявления",
            operationId = "removeAd")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> deleteAdvert(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление информации об объявлении",
            operationId = "updateAds")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> updateAdvert(@PathVariable long id,
                                          @RequestBody CreateOrUpdateAdDto advert) {
        return ResponseEntity.ok(service.update(id, advert));

    }

    @GetMapping("/me")
    @Operation(summary = "Получение объявлений авторизованного пользователя",
            operationId = "getAdsMe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<?> getAdvertsAuthorizedUser(@RequestBody AdvertsDto ads) {
        return null;
    }

    @PatchMapping("/{id}/image")
    @Operation(summary = "Обновление картинки объявления",
            operationId = "updateImage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> UpdateAdvertsPicture(@PathVariable Integer id) {
        return null;
    }
}
