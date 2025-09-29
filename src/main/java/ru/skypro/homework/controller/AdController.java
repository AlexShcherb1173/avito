package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;

@Slf4j
@RestController
@RequestMapping("/ads")
public class AdController {

    //получение всех объявлений
    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        AdsDto adsDto = new AdsDto();
        return ResponseEntity.ok(adsDto);
    }

    //добавление объявления
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAd(@RequestBody CreateOrUpdateAdDto createOrUpdateAdDto,
                                       @RequestParam("image") MultipartFile image) {
        AdDto adDto = new AdDto();
        adDto.setTitle(createOrUpdateAdDto.getTitle());
        adDto.setPrice(createOrUpdateAdDto.getPrice());
        if (true) {
            return ResponseEntity.ok(adDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    //получение информации об объявлении
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDto> getAds(@PathVariable Integer id) {
        ExtendedAdDto extendedAdDto = new ExtendedAdDto();
        if (id == null) {
            return ResponseEntity.notFound().build();   //404
        }
        if (true) {
            return ResponseEntity.ok(extendedAdDto);       //200
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  //401
        }
    }

    //удаление объявления
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable("id") Integer adId) {
        return ResponseEntity.noContent().build();
    }

    //обновление информации об объявлении
    @PatchMapping("/{id}")
    public ResponseEntity<AdDto> updateAds(@PathVariable("id") Integer adId,
                                           @RequestBody CreateOrUpdateAdDto createOrUpdateAdDto) {
        AdDto adDto = new AdDto();
        adDto.setTitle(createOrUpdateAdDto.getTitle());
        adDto.setPrice(createOrUpdateAdDto.getPrice());
        return ResponseEntity.ok(adDto);
    }

    //получение объявлений авторизированного пользователя
    @GetMapping("/me")
    public ResponseEntity<AdsDto> getAdsMe() {
        AdsDto adsDto = new AdsDto();
        return ResponseEntity.ok(adsDto);
    }

    //обновление картинки объявления
    @Operation(
            summary = "Обновление картинки объявления",
            description = "Загружает новое изображение для указанного объявления"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(@PathVariable("adId") Integer adId,
                                              @RequestParam("image") MultipartFile image) {
        try {
            byte[] imageData = new byte[100];
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(imageData);
        } catch(RuntimeException e){
            log.error("Error updating image for ad: {}", adId, e);
            return ResponseEntity.notFound().build();   //404
        }
    }
}
