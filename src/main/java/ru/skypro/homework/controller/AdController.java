package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdService;

import java.io.IOException;

import static java.util.Arrays.asList;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdController {

    private final AdService adService;

    Logger logger = LoggerFactory.getLogger(AdController.class);

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @Operation(summary = "Получение всех объявлений", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ads.class)
                            )),
            })
    @GetMapping
    public Ads getAllAds() {
        logger.debug("\"Get\" getAllAds method was invoke...");
        return adService.getAds();
    }

    @PostMapping
    public Ad addAd(@RequestBody CreateOrUpdateAd updatedAd, @RequestBody MultipartFile adFile) throws IOException {

        logger.debug("\"Post\" addAd method was invoke...");
//                                                                              Здесь будет вызов сервиса
        return adService.createAdWithImage(updatedAd, adFile);
    }

    @Operation(summary = "Получение информации об объявлении", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExtendedAd.class)
                            )),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            })
    @GetMapping("/{id}")
    public ExtendedAd getAds(@PathVariable Long id) {
        logger.debug("\"Get\" getAds method was invoke...");
        return adService.getExtendedAd(id);
    }

    @Operation(summary = "Удаление объявления", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content"
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
            })
    @DeleteMapping("/{id}")
    public void removeId(@PathVariable Long id) {
        logger.debug("\"Delete\" removeId method was invoke...");
        adService.delete(id);
    }

    @Operation(summary = "Обновление информации об объявлении", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ad.class)
                            )),
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
            })
    @PatchMapping("/{id}")
    public Ad updateAds(@PathVariable Long id, @RequestBody CreateOrUpdateAd updatedAd) {
        logger.debug("\"Patch\" updateAds method was invoke...");
        return adService.update(id, updatedAd);
    }

    @GetMapping("/me")
    public Ads getAdsMe() {

        logger.debug("\"Get\" getAdsMe method was invoke...");
//                                                                              Здесь будет вызов сервиса
        Ads ads = (Ads) asList(0, asList(0, "image", 0, 0, "title"));

        return ads;
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(@PathVariable Long id
            , @RequestParam MultipartFile adFile) throws IOException {

        logger.debug("\"Patch\" updateImage method was invoke...");
//                                                                              Здесь будет вызов сервиса
        byte[] adFileUpdated = {0};
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image"));
        headers.setContentLength(adFileUpdated.length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(adFileUpdated);
    }

}
