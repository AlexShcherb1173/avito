package ru.skypro.homework.service;

import ru.skypro.homework.dto.AdvertDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;

public interface AdvertService {
    AdvertDto createAdvert(CreateOrUpdateAdDto createOrUpdateAdDto);

    AdvertDto getAdvertById(Long id);
}
