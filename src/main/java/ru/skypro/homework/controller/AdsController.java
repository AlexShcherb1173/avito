package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.util.Collections;

@Tag(name = "Объявления")
@RestController
@RequestMapping("/ads")
public class AdsController {

    @Operation(summary = "Получение всех объявлений", operationId = "getAllAds")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class)))
    })
    @GetMapping
    public Ads getAllAds() {
        Ads ads = new Ads();
        ads.setCount(0);
        ads.setResults(Collections.emptyList());
        return ads;
    }

    @Operation(summary = "Добавление объявления", operationId = "addAd")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Ad addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                    @RequestPart("image") MultipartFile image) {
        return new Ad();
    }

    @Operation(summary = "Получение информации об объявлении", operationId = "getAds")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExtendedAd.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/{id}")
    public ExtendedAd getAds(@PathVariable Integer id) {
        return new ExtendedAd();
    }

    @Operation(summary = "Удаление объявления", operationId = "removeAd")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void removeAd(@PathVariable Integer id) {
    }

    @Operation(summary = "Обновление информации об объявлении", operationId = "updateAds")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PatchMapping("/{id}")
    public Ad updateAds(@PathVariable Integer id,
                        @RequestBody CreateOrUpdateAd request) {
        return new Ad();
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя", operationId = "getAdsMe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public Ads getAdsMe() {
        Ads ads = new Ads();
        ads.setCount(0);
        ads.setResults(Collections.emptyList());
        return ads;
    }

    @Operation(summary = "Обновление картинки объявления", operationId = "updateImage")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/octet-stream",
                            array = @ArraySchema(schema = @Schema(type = "string", format = "byte")))),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PatchMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id,
                                              @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new byte[0]);
    }


}
