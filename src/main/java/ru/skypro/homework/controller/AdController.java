package ru.skypro.homework.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.AdDto;
import ru.skypro.homework.responseDto.AdsResponse;
import ru.skypro.homework.responseDto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;
    private final UserRepository userRepository;

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
            @AuthenticationPrincipal UserDetails userDetails) {

        // 🔍 Проверяем, что пользователь аутентифицирован
        System.out.println("👤 UserDetails в контроллере: " + userDetails);
        if (userDetails == null) {
            System.out.println("❌ Ошибка: пользователь не аутентифицирован. Проверьте токен в Authorization");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            System.out.println("✅ Начало создания объявления");
            System.out.println("👤 Автор: " + userDetails.getUsername());
            System.out.println("📝 DTO получено: " + adDto);
            System.out.println("🖼 Изображение: " + image.getOriginalFilename() + " (" + image.getSize() + " bytes)");

            // ✅ Получаем сущность User из БД по username
            String username = userDetails.getUsername();
            User dbUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found in database"));

            // ✅ Передаём dbUser в сервис
            AdDto ad = adService.createAd(dbUser, adDto, image);

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