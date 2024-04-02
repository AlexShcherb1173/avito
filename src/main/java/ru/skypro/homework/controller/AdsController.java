package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {


    private final AdsService adsService;
    private final AdsMapper adsMapper;

    /**
     * @return все объявления
     */
    @Operation(summary = "Получение всех объявлений")
    @ApiResponse(responseCode = "200",
            description = "OK",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AdsDto.class)))

    @GetMapping()
    public ResponseEntity<AdsDto> getAllAds() {
        log.info("Запрос всех обьявлений");
        return ResponseEntity.ok(adsMapper.listAdsToAdsDto(adsService.getAllAds().size(), adsService.getAllAds()));
        //        return ResponseEntity.ok().build();
    }

    /**
     * @param createOrUpdateAdDto заголовок, цена, описание
     * @param file изображение
     * @param authentication авторизация
     * @return
     * добавление объявления
     */
    @Operation(summary = "Добавление объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Объявление добавлено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateOrUpdateAdDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован")})

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAd(@Valid @RequestPart(name = "properties") CreateOrUpdateAdDto createOrUpdateAdDto,
                                       @RequestPart(name = "image", required = false) MultipartFile file,
                                       Authentication authentication) {
        try {
            log.info("Добавление объявления");
            return ResponseEntity.ok(adsMapper.adsToAdsDto(adsService.saveAd(createOrUpdateAdDto, authentication.getName(), file)));
        } catch (Exception e){
            log.error("Ошибка при добавлении объявления", e);
            throw new AdsNotFoundException("Ошибка при добавлении объявления", e);
        } //TODO привести в норму catch
    }


    /**
     * @param id id объявления
     * @return информация об объявлении
     */
    @Operation(summary = "Получение информации об объявлении")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExtendedAdDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")

    })
    @GetMapping("/{id}")

    public ResponseEntity<ExtendedAdDto> getInfoAd(@Parameter(in = ParameterIn.PATH, description = "id объявления",
            required=true, schema=@Schema()) @PathVariable("id") Integer id
    )  {
        log.info("Получение информации об объявлении по id");
        return ResponseEntity.ok(adsMapper.toExtendedAdDto(adsService.getAd(id)));
    }

    /**
     * @param id id объявления
     * @param authentication авторизация
     * @return
     * удаление объявления
     */
    @Operation(summary = "Удаление объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable @Parameter(description = "id объявления",
            required = true) Integer id, Authentication authentication) {
        log.info("Удаление объявления по id");
        if (adsService.removeAd(authentication.getName(), id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //TODO избыточность, можно убрать
    }

    /**
     * @param id id объявления
     * @param authentication авторизация
     * @param createOrUpdateAdDto заголовок, цена, описание
     * @return
     */
    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })

    @PatchMapping("/{id}")
    public ResponseEntity<AdDto> updateInfoAd(@PathVariable
                                              @Parameter(description = "id объявления",
                                                      required = true) Integer id, Authentication authentication,
                                              @RequestBody CreateOrUpdateAdDto createOrUpdateAdDto) {
        AdDto adDto = adsMapper.adsToAdsDto(adsService.updateAds(id, createOrUpdateAdDto, authentication.getName()));
        if (adDto != null) {
            return ResponseEntity.ok(adDto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // TODO избыточность
    }

    /**
     * @param authentication авторизация
     * @return объявления пользователя
     */
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdsDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @GetMapping("/me")
    public ResponseEntity<AdsDto> getAllUserAds(@NotNull Authentication authentication) {
        log.info("метод получения всех объявлений авторизованного пользователя");
        AdsDto ads = adsService.getAllUserAds(authentication);
        log.info(String.valueOf(ads));
        if (ads != null) {
            return ResponseEntity.ok(ads);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    /**
     * @param authentication авторизация
     * @param id id объявления
     * @param image изображение
     * @return
     */
    @Operation(summary = "Обновление картинки объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ExtendedAdDto.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })


    @PatchMapping(value = "/{id}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateImageAd(@NotNull Authentication authentication,
                                         @PathVariable("id") int id,
                                         @RequestPart(value = "image") @Valid MultipartFile image
    ) {
        if (adsService.updateImageAd(id, image, authentication.getName())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}




