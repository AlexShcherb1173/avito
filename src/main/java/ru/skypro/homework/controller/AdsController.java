package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdRequestDto;
import ru.skypro.homework.dto.ads.AdResponseDto;
import ru.skypro.homework.dto.ads.AdsDto;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("ads")
@RequiredArgsConstructor
public class AdsController {

    @GetMapping("/")
    public AdsDto getAllAds() {
        //TODO: implement

        return new AdsDto(0, List.of(
                new AdDto(1, "example/image.jpg", 1, new BigDecimal(100), "example title")
        ));
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AdDto createAd(@RequestBody AdRequestDto req, MultipartFile image) {
        //TODO: implement

        return new AdDto(1, "example/image.jpg", 1, new BigDecimal(100), "example title");
    }

    @GetMapping("/{id}")
    public AdResponseDto getAd(@PathVariable long id) {
        //TODO: implement
        return new AdResponseDto(
                1,
                "null",
                "null",
                "null",
                "null@null.com",
                "example/image.jpg",
                "null",
                new BigDecimal(100),
                "null"
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAd(@PathVariable long id) {
        //TODO: implement
    }

    @PatchMapping("/{id}")
    public AdDto updateAd(@PathVariable long id, @RequestBody AdRequestDto req) {
        //TODO: implement

        return new AdDto(1, "example/image.jpg", 1, new BigDecimal(100), "example title");
    }

    @GetMapping("/me")
    public AdsDto getCurrentUserAds(@AuthenticationPrincipal UserDetails user) {
        //TODO: implement

        // Можно передавать как параметр для сервиса
        String userName = user.getUsername();

        return new AdsDto(0, List.of(
                new AdDto(1, "example/image.jpg", 1, new BigDecimal(100), "example title")
        ));
    }

    @PatchMapping("/{id}/image")
    public String updateAdImage(@PathVariable long id, MultipartFile image) {
        //TODO: implement

        return "example/image.jpg";
    }
}
