package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AdService {
    AdsDTO getAllAds();

    AdDTO addAd(CreateOrUpdateAdDTO properties, MultipartFile image);

    ExtendedAdDTO getAd(Integer Id);

    void deleteAd(Integer id);

    AdDTO updateAd(Integer id, CreateOrUpdateAdDTO ad);

    AdsDTO getUserAds();

    void updateAdImage(Integer id, MultipartFile image);

    void downloadAvatarFromFileSystem(int id, HttpServletResponse response) throws IOException;
}
