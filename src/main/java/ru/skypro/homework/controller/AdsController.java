package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.Ad;

@Tag(name = "Ads", description = "Controller for managing advertisements")
@RestController
@RequestMapping("/ads")
public class AdsController {

    @Operation(summary = "Get all ads", description = "Retrieves a list of all advertisements")
    @GetMapping
    public Ads getAds() {
        return new Ads(0, List.of());
    }

    @Operation(summary = "Create an ad", description = "Creates a new advertisement")
    @PostMapping
    public Ad createAd(@RequestBody Ad ad, @RequestParam String image) {
        return ad;
    }
    @Operation(summary = "Get ad by ID", description = "Retrieves an advertisement by its ID")
    @GetMapping("/{id}")
    public Ad getAd(@PathVariable int id) {
        return new Ad();
    }
    @Operation(summary = "Delete ad by ID", description = "Deletes an advertisement by its ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAd(@PathVariable int id) {
        
    }
    @Operation(summary = "Update ad by ID", description = "Updates an advertisement by its ID")
    @PatchMapping("/{id}")
    public Ad updateAd(@PathVariable int id, @RequestBody Ad ad) {
        return ad;
    }

    @Operation(summary = "Get user's ads", description = "Retrieves all advertisements created by the user")
    @GetMapping("/me")
    public Ads getMyAds() {
        return new Ads(0, List.of());
    }
    @Operation(summary = "Update ads image", description = "Sets a new image for an advertisement")
    @PatchMapping("/{id}/image")
    public Ad updateAdImage(@PathVariable int id, @RequestBody String image) {
        return new Ad();
    }
}
