package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ads")
@CrossOrigin(
        origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS }
)
@Schema(description = "Объявления")
public class AdController {
    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping
    @Operation(summary = "Get all ads", description = "получение всех объявлений")
    public List<AdDTO> getAllAds() {
        return adService.getAllAds();
    }

    @PostMapping
    @Operation(summary = "Add new ad", description = "добавление нового объявления",
            parameters = {
                @Parameter(name = "ad", description = "new ad to add"),
                @Parameter(name = "image", description = "image for new ad")
            },
            responses = {
                @ApiResponse(responseCode = "201", description = "Created"),
                @ApiResponse(responseCode = "400", description = "Some fields haven't passed validation"),
                @ApiResponse(responseCode = "401", description = "Unauthorized") })
    public ResponseEntity<AdDTO> addAd(
            @RequestPart("properties") CreateOrUpdateAd dto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        Ad ad = adService.addAd(dto, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(AdMapper.INSTANCE.adToAdDto(ad));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
        Ad ad = adService.getAdById(id);
        if (ad.getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(ad.getImage());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ad by id", description = "получение объявления по id",
            parameters = @Parameter(name = "id", description = "id of ad to get"),
            responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "404", description = "Not found")})
    public AdDTO getAd(@PathVariable int id) {
        return new AdDTO();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ad by id", description = "удаление объявления по id",
            parameters = @Parameter(name = "id", description = "id of ad to delete"),
            responses = {
                @ApiResponse(responseCode = "204", description = "No Content"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "403", description = "Forbidden"),
                @ApiResponse(responseCode = "404", description = "Not found")})
    public void deleteAd(@PathVariable int id) {
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update ad by id", description = "обновление объявления по id",
            parameters = {@Parameter(name = "id", description = "id of ad to update")},
            responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(responseCode = "400", description = "Some fields haven't passed validation"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "403", description = "Forbidden"),
                @ApiResponse(responseCode = "404", description = "Not found")})
    public CreateOrUpdateAd updateAd(@PathVariable int id) {
        return new CreateOrUpdateAd();
    }

    @GetMapping("/me")
    @Operation(summary = "Get my ads", description = "получение объявлений автора",
            responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(responseCode = "401", description = "Unauthorized")})
    public List<AdDTO> getMyAds() {
        return adService.getAllAds();
    }

    @PatchMapping("/{id}/image")
    @Operation(summary = "Update image of ad", description = "обновление картинки объявления",
            parameters = {
                    @Parameter(name = "id", description = "id of ad to update", required = true),
                    @Parameter(name = "image", description = "new image for ad", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public void updateImage(@PathVariable int id, @RequestBody String image) {

    }
}