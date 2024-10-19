package ru.skypro.homework.controller;

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

import java.io.IOException;

import static java.util.Arrays.asList;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {

    Logger logger = LoggerFactory.getLogger(AdsController.class);

    @GetMapping
    public ResponseEntity<Ads> getAds() {

        logger.debug("\"Get\" getAds method was invoke...");
//                                                                              Здесь будет вызов сервиса
        Ads ads = (Ads) asList(0, asList(0, "image", 0, 0, "title"));

        return ResponseEntity.ok(ads);
    }

    @PostMapping
    public ResponseEntity<Void> postAd(@RequestBody Ad ad) {

        logger.debug("\"Post\" postAd method was invoke...");
//                                                                              Здесь будет вызов сервиса
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAdById(@PathVariable int id) {

        logger.debug("\"Get\" getAdById method was invoke...");
//                                                                              Здесь будет вызов сервиса
        ExtendedAd eAd = (ExtendedAd) asList(0, "authorFirstName", "authorLastName"
                , "description", "email", "image", "phone", 0, "title");

        return ResponseEntity.ok(eAd);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ad> deleteAdById(@PathVariable int id) {

        logger.debug("\"Delete\" deleteAdById method was invoke...");
//                                                                              Здесь будет вызов сервиса
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ad> patchAdById(@PathVariable int id, @RequestBody CreateOrUpdateAd cAd) {

        logger.debug("\"Patch\" patchAdById method was invoke...");
//                                                                              Здесь будет вызов сервиса
        Ad ad = (Ad) asList(0, "image", 0, 0, "title");

        return ResponseEntity.ok(ad);
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe() {

        logger.debug("\"Get\" getAdsMe method was invoke...");
//                                                                              Здесь будет вызов сервиса
        Ads ads = (Ads) asList(0, asList(0, "image", 0, 0, "title"));

        return ResponseEntity.ok(ads);
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> patchImageAdById(@PathVariable int id
            , @RequestParam MultipartFile adFile) throws IOException {

        logger.debug("\"Patch\" patchImageAdById method was invoke...");
//                                                                              Здесь будет вызов сервиса
        byte[] adFileUpdated = {0};
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image"));
        headers.setContentLength(adFileUpdated.length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(adFileUpdated);
    }

}
