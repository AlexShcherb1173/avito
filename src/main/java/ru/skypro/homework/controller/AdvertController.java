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
            operationId = "getAllAdverts")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<?> getAllAdverts() {
        return ResponseEntity.ok(service.getAllAdverts());
    }

    @PostMapping
    @Operation(summary = "Добавление объявления",
            operationId = "createAdvert")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<?> createAdvert(@RequestPart CreateOrUpdateAdDto advert,
                                     @RequestPart(name = "image") MultipartFile file) {
        return new ResponseEntity<>(service.createAdvert(advert, file), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение комментариев объявления",
            operationId = "getAdvertById")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> getAdvertById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAdvertInfo(id));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удаление объявления",
            operationId = "deleteAdvert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> deleteAdvert(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteAdvert(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление информации об объявлении",
            operationId = "updateAdvert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> updateAdvert(@PathVariable Long id,
                                          @RequestBody CreateOrUpdateAdDto advert) {
        return ResponseEntity.ok(service.updateAdvert(id, advert));
    }

    @GetMapping("/me")
    @Operation(summary = "Получение объявлений авторизованного пользователя",
            operationId = "getAdsMe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<AdvertsDto> getAdvertsAuthorizedUser() {
        return ResponseEntity.ok(service.getAllAdvertsByAuthor());
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
    public ResponseEntity<?> UpdateAdvertImage(@PathVariable Long id,@RequestPart MultipartFile image) {
        return ResponseEntity.ok(service.updateAdvertImage(id, image));
    }
}
