package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.service.AdService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.skypro.homework.roles.RoleAuthority.*;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "Методы для работы с объявлениями")
public class AdController {
    private final AdService adService;

    AdController(AdService adService) {
        this.adService = adService;
    }

    @Operation(summary = "Получить все объявления", description = "Возвращает список всех объявлений")
    @PreAuthorize(ALL)
    @GetMapping
    public ResponseEntity<AdsDTO> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Operation(summary = "Добавить объявление", description = "Создаёт новое объявление")
    @PreAuthorize(USER)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDTO> addAd(@RequestPart("properties") CreateOrUpdateAdDTO properties,
                                       @RequestPart("image") MultipartFile image) {
        AdDTO adDTO = adService.addAd(properties, image);
        return ResponseEntity.status(200).body(adDTO);
    }

    @Operation(summary = "Получить объявление по ID", description = "Возвращает объявление по указанному ID")
    @PreAuthorize(ALL)
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDTO> getAd(@PathVariable Integer id) {
        return ResponseEntity.ok(adService.getAd(id));
    }

    @Operation(summary = "Удалить объявление", description = "Удаляет объявление по указанному ID")
    @PreAuthorize(USER + " and @adSecurityService.isOwner(#id) or " + ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Integer id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить объявление", description = "Обновляет информацию об объявлении по ID")
    @PreAuthorize(USER + " and @adSecurityService.isOwner(#id) or " + ADMIN)
    @PatchMapping("/{id}")
    public ResponseEntity<AdDTO> updateAds(@PathVariable Integer id, @RequestBody CreateOrUpdateAdDTO ad) {
        return ResponseEntity.ok(adService.updateAd(id, ad));
    }

    @Operation(summary = "Получить мои объявления", description = "Возвращает список объявлений, созданных текущим пользователем")
    @PreAuthorize(USER)
    @GetMapping("/me")
    public ResponseEntity<AdsDTO> getUserAds() {
        return ResponseEntity.ok(adService.getUserAds());
    }

    @Operation(summary = "Обновить изображение объявления", description = "Обновляет картинки объявления")
    @PreAuthorize(USER + " and @adSecurityService.isOwner(#id) or " + ADMIN)
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/{id}/image")
    public ResponseEntity<Void> updateImage(@PathVariable Integer id,
                                            @RequestPart("image") MultipartFile image) {
        adService.updateAdImage(id, image);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить изображение объявления", description = "Позволяет получить изображение объявления")
    @PreAuthorize(ALL)
    @GetMapping(value = "/{id}/image/get")
    public void downloadAvatarFromFileSystem(@PathVariable int id, HttpServletResponse response)
            throws IOException {
        adService.downloadAvatarFromFileSystem(id, response);
    }
}