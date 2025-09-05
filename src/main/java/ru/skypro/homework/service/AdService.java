package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

public interface AdService {
    Ads getAll();
    AdDto create(CreateOrUpdateAd dto, MultipartFile image);
    ExtendedAd getById(Integer id);
    AdDto update(Integer id, CreateOrUpdateAd dto);
    void delete(Integer id);
    Ads getMy();
}