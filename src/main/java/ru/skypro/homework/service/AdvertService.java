package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdvertDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;

public interface AdvertService {

    AdvertDto createAdvert(CreateOrUpdateAdDto createOrUpdateAdDto, MultipartFile file);

    AdvertDto getAdvertById(Long id);
}
