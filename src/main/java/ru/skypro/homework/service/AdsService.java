package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;

import java.util.List;

public interface AdsService {

    List<Ad> getAllAds();

    Ad addAd(Ad ad);

    Ad getAdById(Long id);

    boolean deleteAd(Long id);

    Ad updateAd(Long id, Ad updatedAd);

    List<Ad> getMyAds();

    boolean updateAdImage(Long id, MultipartFile image);
}
