package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;

    public AdServiceImpl(AdRepository adRepository,
                         AdMapper adMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
    }

    @Override
    public List<Ad> getAllAds() {
        return adRepository.findAll()
                .stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Ad getAd(Integer id) {
        return adRepository.findById(id)
                .map(adMapper::toDto)
                .orElseThrow(IllegalStateException::new);
    }
}
