package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.AdDto;
import ru.skypro.homework.responseDto.AdsResponse;
import ru.skypro.homework.responseDto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;

@RestController
@Transactional
@RequestMapping("/ads")
@Tag(name = "Ads", description = "API для работы с объявлениями: создание, просмотр, редактирование, удаление")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;
    private final UserRepository userRepository;

    @Operation(summary = "Получение всех объявлений", description = "Возвращает список всех объявлений с пагинацией.")
    @GetMapping
    public AdsResponse getAllAds() {
        return adService.getAllAds();
    }

    @Operation(summary = "Получение своих объявлений", description = "Возвращает все объявления текущего пользователя.")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public AdsResponse getMyAds(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        return adService.getMyAds(userDetails.getUsername());
    }

    @Operation(
            summary = "Создание объявления",
            description = "Создаёт новое объявление с заголовком, ценой, описанием и изображением.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "object"),
                            encoding = {
                                    @Encoding(name = "properties", contentType = "application/json"),
                                    @Encoding(name = "image", contentType = "image/jpeg, image/png")
                            }
                    )
            )
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public AdDto addAd(
            @RequestPart("properties") @Valid CreateOrUpdateAd adDto,
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        return adService.createAdFromMultipart(userDetails, adDto, image);
    }

    @Operation(summary = "Полная информация об объявлении", description = "Возвращает расширенные данные объявления: автор, описание, изображение и т.д.")
    @GetMapping("/{id}")
    @PreAuthorize("@adServiceImpl.isOwner(#id, authentication.principal.username) or hasRole('ADMIN')")
    public ExtendedAdDto getAd(@PathVariable Long id) {
        ExtendedAdDto dto = adService.getExtendedAd(id);
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found");
        }
        return dto;
    }

    @Operation(summary = "Удаление объявления", description = "Удаляет объявление по ID. Только автор или администратор может удалить.")
    @DeleteMapping("/{id}")
    @PreAuthorize("@adServiceImpl.isOwner(#id, authentication.principal.username) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAd(@PathVariable Long id) {
        adService.deleteAd(id);
    }

    @Operation(summary = "Редактирование объявления", description = "Изменяет заголовок и цену объявления. Автор остаётся прежним.")
    @PatchMapping("/{id}")
    @PreAuthorize("@adServiceImpl.isOwner(#id, authentication.principal.username) or hasRole('ADMIN')")
    public AdDto updateAd(
            @PathVariable Long id,
            @RequestBody @Valid CreateOrUpdateAd dto) {
        AdDto updated = adService.updateAd(id, dto);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found");
        }
        return updated;
    }

    @Operation(summary = "Обновление изображения объявления", description = "Заменяет изображение у существующего объявления.")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@adServiceImpl.isOwner(#id, authentication.principal.username) or hasRole('ADMIN')")
    public String updateAdImage(
            @PathVariable Long id,
            @RequestPart("image") MultipartFile image) {
        String imageUrl = adService.updateAdImage(id, image);
        if (imageUrl == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found");
        }
        return imageUrl;
    }
}