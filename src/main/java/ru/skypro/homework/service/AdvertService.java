package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdvertDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;

public interface AdvertService {

    AdvertDto create(CreateOrUpdateAdDto createOrUpdateAdDto, MultipartFile file);

    void delete(long id);

    AdvertDto update(long id, CreateOrUpdateAdDto createOrUpdateAdDto);

    AdvertDto getAdvertById(long id);
}
