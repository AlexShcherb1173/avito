package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.impl.AdServiceImpl;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = " Объявления ", description = "API для работы с объявлениями")
public class AdsController {

    private final AdServiceImpl adService;

    private static final Logger log = LoggerFactory.getLogger(AdsController.class);

    public AdsController(AdServiceImpl adService) {
        this.adService = adService;
    }


    @Operation(summary = "получение всех объявлений", responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class)))
    }
    )
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        log.info(" Получить все объявления по названию ");
        Ads ads = adService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    @Operation(summary = "добавление объявления", responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    }
    )
    @PostMapping
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd properties, @RequestPart("image") MultipartFile image, Authentication authentication) {
        log.info("Добавить объявления", authentication.name());
        try {
            Ad ad = adService.addAd(properties, image.getBytes(), authentication);
            return ResponseEntity.ok(ad);
        }  catch (Exception e){
            log.error(" Ошибка добавления объявления ", e);
            return ResponseEntity.badRequest().build();
        }

    }

    @Operation(summary = "получение информации об объявлении", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExtendedAd.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {
        log.info(" Получить все объявления по id" + id);
        ExtendedAd extendedAd = adService.getAd(id);
        return ResponseEntity.ok(extendedAd);
    }

    @Operation(summary = "удаление объявления", responses = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable Integer id){
        log.info(" Удалить объявления " + id);
        adService.removeAd(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "обновление информации об объявлении", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable Integer id, @RequestBody CreateOrUpdateAd createOrUpdateAd){
        log.info(" Обновить объявления " + id);
        Ad ad = adService.updateAd(id, createOrUpdateAd);
        return ResponseEntity.ok(ad);
    }

    @Operation(summary = "получение объявлений авторизованного пользователя", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    }
    )
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication){
        log.info("Получить мои объявления ", authentication.name());
        try {
            Ads ads = adService.getAdsMe(authentication);
            return ResponseEntity.ok(ads);
        } catch (Exception e) {
            log.error(" Ошибка получения моих объявлений ");
            return ResponseEntity.badRequest().build();
        }


    }


}
