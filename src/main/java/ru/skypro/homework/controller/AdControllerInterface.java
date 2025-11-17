package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;

import javax.validation.Valid;
import java.io.IOException;

public interface AdControllerInterface {
    @GetMapping
    @Operation(operationId = "getAllAds", summary = "Получение всех объявлений", tags = {"Объявления"})
    @ApiResponse(responseCode = "200", description = "OK")
    AdsDto getAllAds();

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    @Operation(operationId = "addAd", summary = "Добавление объявления", tags = {"Объявления"})
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    AdDto addAd(@RequestPart("properties") @Valid CreateOrUpdateAdDto properties, @RequestPart("image") MultipartFile image) throws IOException;

    @GetMapping("/{id}")
    @Operation(operationId = "getAds", summary = "Получение информации об объявлении", tags = {"Объявления"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    ExtendedAdDto getAds(@PathVariable(name = "id") Integer id);

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.findAdById(#id).user.email == authentication.name")
    @Operation(operationId = "removeAd", summary = "Удаление объявления", tags = {"Объявления"})
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    void removeAd(@PathVariable(name = "id") Integer id);

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.findAdById(#id).user.email == authentication.name")
    @Operation(operationId = "updateAds", summary = "Обновление информации об объявлении", tags = {"Объявления"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    AdDto updateAds(@PathVariable(name = "id") Integer id, @RequestBody CreateOrUpdateAdDto createOrUpdateAdDto);

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Operation(operationId = "getAdsMe", summary = "Получение объявлений авторизованного пользователя", tags = {"Объявления"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    AdsDto getAdsMe();

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@adServiceImpl.findAdById(#id).user.email == authentication.name")
    @Operation(operationId = "updateImage", summary = "Обновление картинки объявления", tags = {"Объявления"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    byte[] updateImage(@PathVariable(name = "id") Integer id, @RequestParam("image") MultipartFile image) throws IOException;
}
