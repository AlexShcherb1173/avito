package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;

    public AdsDto getAllAds() {
        List<Ad> ads = adRepository.findAll();
        AdsDto result = new AdsDto();
        result.setCount(ads.size());
        result.setResults(ads.stream().map(AdMapper::toDto).collect(Collectors.toList()));
        return result;
    }

    public AdDto getAdById(Integer id) {
        return adRepository.findById(id)
                .map(AdMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
    }

    public AdDto createAd(Authentication authentication, CreateOrUpdateAdDto dto) {
        User author = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Ad ad = AdMapper.toEntity(dto, author);
        adRepository.save(ad);

        return AdMapper.toDto(ad);
    }

    public void deleteAd(Authentication authentication, Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        if (!ad.getAuthor().getEmail().equals(authentication.getName()) &&
                authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Нет прав для удаления объявления");
        }

        adRepository.delete(ad);
    }

    public AdDto updateAd(Authentication authentication, Integer id, CreateOrUpdateAdDto dto) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        if (!ad.getAuthor().getEmail().equals(authentication.getName()) &&
                authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Нет прав для редактирования объявления");
        }

        ad.setTitle(dto.getTitle());
        ad.setPrice(dto.getPrice());
        ad.setDescription(dto.getDescription());
        adRepository.save(ad);

        return AdMapper.toDto(ad);
    }
}