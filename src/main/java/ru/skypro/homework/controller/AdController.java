package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.security.UserPrincipal;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.response.AdResponse;
import ru.skypro.homework.dto.response.AdsResponse;
import ru.skypro.homework.dto.response.ExtendedAdResponse;
import ru.skypro.homework.mapper.*;
import ru.skypro.homework.service.AdServiceApi;
import ru.skypro.homework.utils.ValidationUtils;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdServiceApi adService;
    private final AdMapper adMapper;
    private final AdsMapper adsMapper;
    private final CreateOrUpdateAdMapper createOrUpdateAdMapper;
    private final ExtendedAdResponseMapper extendedAdResponseMapper;
    private final ImageMapper imageMapper;

    @Operation(
            tags = "Объявления",
            summary = "Получение всех объявлений",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = AdsResponse.class
                                    )
                            )
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.ok().body(adsMapper.toAdsResponse(adService.getAllAds()));
    }

    @Operation(
            tags = "Объявления",
            summary = "Добавление объявления",
            operationId = "addAd",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = "multipart/form-data",
                                    encoding = @Encoding(
                                            name = "properties",
                                            contentType = "application/json"
                                    )
                            )
                    }
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = AdResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(hidden = true)
                            )
                    )
            }

    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAd(
            @RequestPart MultipartFile image,
            @RequestPart @Valid CreateOrUpdateAd properties,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ValidationUtils.createErrorResponse(bindingResult.getAllErrors(), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok().body(
                adMapper.mappingToDto(
                        adService.createAdWithImage(
                                createOrUpdateAdMapper.toAd(properties),
                                imageMapper.toImage(image))
                )
        );
    }

    @Operation(
            tags = "Объявления",
            summary = "Получение информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ExtendedAdResponse.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdById(@PathVariable Long id) {
        return ResponseEntity.ok(extendedAdResponseMapper.toDto(adService.getAdById(id)));
    }

    @Operation(
            tags = "Объявления",
            summary = "Удаление объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ExtendedAdResponse.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #user.user.id == @adService.getAdById(#id).user.id")
    public ResponseEntity<?> deleteAd(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        try {
            adService.deleteAd(id);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(
            tags = "Объявления",
            summary = "Обновление информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CreateOrUpdateAd.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BAD_REQUEST"
                    )
            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #user.user.id == @adService.getAdById(#id).user.id")
    public ResponseEntity<?> updateAdInfo(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id,
            @RequestBody @Valid CreateOrUpdateAd properties,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ValidationUtils.createErrorResponse(bindingResult.getAllErrors(), HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok().body(adMapper.mappingToDto(adService.updateAdDetails(id, properties)));
    }

    @Operation(
            tags = "Объявления",
            summary = "Получение объявлений авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AdsResponse.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    @GetMapping("/me")
    public ResponseEntity<?> getAdsByAuthUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok().body(adsMapper.toAdsResponse(adService.getUserAds(userPrincipal.getUser())));
    }

    @Operation(
            tags = "Объявления",
            summary = "Обновление картинки объявления",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = "multipart/form-data"
                            )
                    }
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/octet-stream",
                                    schema = @Schema(
                                            type = "string",
                                            format = "byte"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }
    )
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or #user.user.id == @adService.getAdById(#id).user.id")
    public ResponseEntity<?> updateImageAds(
            @PathVariable Long id,
            @RequestPart(name = "image") MultipartFile image,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok().body(
                adMapper.mappingToDto(adService.updateAdImage(id, imageMapper.toImage(image)))
        );
    }
}
