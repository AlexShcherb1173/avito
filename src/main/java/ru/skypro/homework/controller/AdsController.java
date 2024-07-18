package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.service.AdsService;

/**
 * Контроллер для работы с объявлениями
 */
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "Эндпойнты для работы с пользователями")
@AllArgsConstructor
public class AdsController {

    private AdsService service;

    @GetMapping
    @Operation(summary = "Получение всех объявлений",
            operationId = "getAllAds")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<?> getAllAds() {
        return null;
    }

    @PostMapping
    @Operation(summary = "Добавление объявления",
            operationId = "addAd")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<?> createAds(@RequestBody AdsDto adsDto) {
        return null;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение комментариев объявления",
            operationId = "getAds")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> getInfoAds(@PathVariable long id) {
        return ResponseEntity.ok("Пользователь получен");
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
    public ResponseEntity<?> deleteAd(@PathVariable long id) {
        return null;
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
    public ResponseEntity<?> updateInfoAd(@PathVariable long id) {
        return null;
    }

    @GetMapping("/me")
    @Operation(summary = "Получение объявлений авторизованного пользователя",
            operationId = "getAdsMe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<?> getAdsAuthorizedUser(@RequestBody AdsDto adsDto) {
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
    public ResponseEntity<?> UpdateAdsPicture(@PathVariable long id) {
        return null;
    }
}
