package ru.skypro.homework.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.AppMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UsersRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Transactional
public class AdsController {
    private final AdRepository adRepository;
    private final UsersRepository usersRepository;
    private final ImageRepository imageRepository;
    private final AppMapper appMapper;

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        List<AdDTO> adDTOs = adRepository.findAll().stream()
                .map(ad -> {
                    AdDTO dto = appMapper.adEntityToAdDTO(ad);
                    dto.setImage(appMapper.convertImageToPath(ad.getImage()));
                    return dto;
                })
                .collect(Collectors.toList());

        Ads response = new Ads();
        response.setCount(adDTOs.size());
        response.setResults(adDTOs);
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDTO> addAd(
            @RequestPart("properties") CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile imageFile,
            Authentication authentication) throws IOException {

        Users author = usersRepository.findById(((Users) authentication.getPrincipal()).getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        Ad ad = appMapper.createOrUpdateAdToAdEntity(properties);
        ad.setAuthor(author);

        Image image = new Image();
        image.setData(imageFile.getBytes());
        image.setMediaType(imageFile.getContentType());
        Image savedImage = imageRepository.save(image);
        ad.setImage(savedImage);

        Ad savedAd = adRepository.save(ad);

        AdDTO adDTO = appMapper.adEntityToAdDTO(savedAd);
        adDTO.setImage(appMapper.convertImageToPath(savedImage));

        return ResponseEntity.status(HttpStatus.CREATED).body(adDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ExtendedAd extendedAd = appMapper.adEntityToExtendedAd(ad);
        extendedAd.setImage(appMapper.convertImageToPath(ad.getImage()));

        return ResponseEntity.ok(extendedAd);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (ad.getImage() != null) {
            imageRepository.delete(ad.getImage());
        }

        adRepository.delete(ad);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdDTO> updateAds(
            @PathVariable Integer id,
            @RequestBody CreateOrUpdateAd updateAd) {

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ad.setTitle(updateAd.getTitle());
        ad.setPrice(updateAd.getPrice());
        ad.setDescription(updateAd.getDescription());

        Ad updatedAd = adRepository.save(ad);

        AdDTO adDTO = appMapper.adEntityToAdDTO(updatedAd);
        adDTO.setImage(appMapper.convertImageToPath(updatedAd.getImage()));

        return ResponseEntity.ok(adDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        Users author = (Users) authentication.getPrincipal();
        List<AdDTO> adDTOs = adRepository.findAllByAuthorId(author.getId()).stream()
                .map(ad -> {
                    AdDTO dto = appMapper.adEntityToAdDTO(ad);
                    dto.setImage(appMapper.convertImageToPath(ad.getImage()));
                    return dto;
                })
                .collect(Collectors.toList());

        Ads response = new Ads();
        response.setCount(adDTOs.size());
        response.setResults(adDTOs);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(
            @PathVariable Integer id,
            @RequestParam("image") MultipartFile imageFile) throws IOException {

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (ad.getImage() != null) {
            imageRepository.delete(ad.getImage());
        }

        Image newImage = new Image();
        newImage.setData(imageFile.getBytes());
        newImage.setMediaType(imageFile.getContentType());
        Image savedImage = imageRepository.save(newImage);
        ad.setImage(savedImage);
        adRepository.save(ad);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(savedImage.getMediaType()))
                .body(savedImage.getData());
    }
}