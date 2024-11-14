package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.service.impl.AdServiceImpl;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@Tag(name = "Объявления")
public class AdController {

    private final AdServiceImpl adServiceImpl;

    public AdController(AdServiceImpl adServiceImpl) {
        this.adServiceImpl = adServiceImpl;
    }

    @Operation(summary = "Получение всех объявлений", tags = {"Объявления"})
    @PreAuthorize("hasRole('ADMIN) or hasRole('USER') and @adService.isOwner(#adId, authentication.name))")
    @GetMapping(path = "/ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class))
            })
    })
    public ResponseEntity<?> getAllAds(Integer id) {
        log.info("Метод getAllAds, класса AdController");
        if (adServiceImpl.existsById(id)) { // Проверка есть ли объявление или нет
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого объявления не существует");
        }
        adServiceImpl.getAllAds();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавление объявления", tags = {"Объявления"})
    @PreAuthorize("hasRole('ADMIN) or hasRole('USER') and @adService.isOwner(#adId, authentication.name))")
    @PostMapping(path = "/ads", consumes = "multipart/from-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = ""))
    })
    public ResponseEntity<?> addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                                   @RequestPart(value = "imagine", required = true) MultipartFile image) {
        log.info("Метод addAds, класса AdController. Приняты: \nНовое объявление {}\nИзображение объявления{}",
                properties.toString(), image.getOriginalFilename());
        try {
            adServiceImpl.addAd(properties, image);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Ошибка при добавлении объявления: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка при добавлении объявления");
        }
    }

    @Operation(summary = "Получение информации об объявлении", tags = {"Объявления"})
    @GetMapping(path = "/ads/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') and @adService.isOwner(#id, authentication.name))")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class)),
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> getAds(@PathVariable int id) {
        log.info("Метод getAdsById, класса AdController. Принят: \n(int) id {}", id);
        if (adServiceImpl.existsById(id)) { // Проверка есть ли объявление или нет
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого объявления не существует");
        }
        AdModel ad = adServiceImpl.getAds(id);
        return ResponseEntity.ok(ad);
    }

    @Operation(summary = "Удаление объявления", tags = {"Объявления"})
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @adService.isOwner(#adId, authentication.name))")
    @DeleteMapping(path = "/ads/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> removeAd(@PathVariable("id") int id) {
        log.info("Метод deleteAdsById, класса AdController. Принят: \n(int) id {}", id);
        if (adServiceImpl.existsById(id)) { // Проверка есть ли объявление или нет
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого объявления не существует");
        }
        adServiceImpl.removeAd(id);
        return ResponseEntity.ok().body("Объявление успешно удалено");
    }

    @Operation(summary = "Обновление информации в объявлении", tags = {"Объявления"})
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @adService.isOwner(#adId, authentication.name))")
    @PatchMapping(path = "/ads/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> updateAd(@PathVariable int id, @RequestBody AdModel adModel) {
        log.info("Метод addAds, класса AdController. Приняты: \n(int) id {}\nНовое объявление или обновление имеющегося {}",
                id, adModel.toString());
        if (adServiceImpl.existsById(id)) { // Проверка есть ли объявление или нет
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого объявления не существует");
        }
        try {
            adServiceImpl.updateAd(id, adModel);
            return ResponseEntity.ok().body("Объявление успешно обновлено");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя", tags = {"Объявления"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') and @adService.isOwner(#adId, authentication.name))")
    @GetMapping(path = "/ads/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> getAdsMe(@PathVariable int id) {
        log.info("Метод getAdsCurrentUser, класса AdController.");
        if (adServiceImpl.existsById(id)) { // Проверка есть ли объявление или нет
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого объявления не существует");
        }
        adServiceImpl.getAdsMe(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление картинки объявления", tags = {"Объявления"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') and @adService.isOwner(#adId, authentication.name))")
    @PatchMapping(path = "/ads/{id}/image", consumes = "multipart/from-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/octet-stream",
                            array = @ArraySchema(schema = @Schema(type = "string", format = "byte")))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> updateImage(@PathVariable("id") int id,
                                         @RequestPart(value = "image", required = true) MultipartFile image) {
        log.info("Метод addAds, класса AdController. Приняты: \n(int) id {}\nИзображение объявления{}",
                id, image.getOriginalFilename());
        if (!adServiceImpl.existsById(id)) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого объявления не существует");
        }
        adServiceImpl.updateImage(id, image);
        return ResponseEntity.ok().body("Изображение успешно обновлено");
    }
}
