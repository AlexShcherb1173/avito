package ru.skypro.homework.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.User;
import ru.skypro.homework.responseDto.AdDto;
import ru.skypro.homework.responseDto.AdsResponse;
import ru.skypro.homework.responseDto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;

import java.io.IOException;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    // Получение всех объявлений
    @GetMapping
    public ResponseEntity<AdsResponse> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    // Получение своих объявлений
    @GetMapping("/me")
    public ResponseEntity<AdsResponse> getMyAds(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(adService.getMyAds(user));
    }

    // Добавление нового объявления
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAd(
            @RequestPart("properties") @Valid CreateOrUpdateAd adDto,
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal User user) {

        // 🔍 Добавьте эти строки в начало метода
        System.out.println("👤 User в контроллере: " + user);
        if (user == null) {
            System.out.println("❌ Ошибка: пользователь не аутентифицирован. Проверьте токен в Authorization");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            System.out.println("✅ Начало создания объявления");
            System.out.println("👤 Автор: " + user.getUsername());
            System.out.println("📝 DTO получено: " + adDto);
            System.out.println("🖼 Изображение: " + image.getOriginalFilename());

            AdDto ad = adService.createAd(user, adDto, image);

            System.out.println("✅ Объявление успешно создано, ID: " + ad.getPk());
            return new ResponseEntity<>(ad, HttpStatus.CREATED);

        } catch (MaxUploadSizeExceededException e) {
            System.out.println("❌ Ошибка: размер файла превышен");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("File size exceeds the allowed limit (10MB)");
        } catch (Exception e) {
            System.out.println("❌ Неизвестная ошибка: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create ad");
        }
    }

    // Получение полной информации об объявлении
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDto> getAd(@PathVariable Long id) {
        ExtendedAdDto dto = adService.getExtendedAd(id);
        return ResponseEntity.ok(dto);
    }

    // Удаление объявления
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    // Редактирование данных объявления
    @PatchMapping("/{id}")
    public ResponseEntity<AdDto> updateAd(
            @PathVariable Long id,
            @RequestBody @Valid CreateOrUpdateAd dto) {
        AdDto updated = adService.updateAd(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Обновление изображения объявления
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateAdImage(
            @PathVariable Long id,
            @RequestPart("image") MultipartFile image) {
        String imageUrl = adService.updateAdImage(id, image);
        return ResponseEntity.ok(imageUrl);
    }
}