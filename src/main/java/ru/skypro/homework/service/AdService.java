package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.mapper.DtoMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    public Ads getAllAds() {
        List<AdEntity> adEntities = adRepository.findAll();
        List<Ad> adDtos = dtoMapper.toAdList(adEntities);

        Ads ads = new Ads();
        ads.setCount(adDtos.size());
        ads.setResults(adDtos);
        return ads;
    }

    public Optional<ExtendedAd> getAdById(Integer id) {
        return adRepository.findById(id)
                .map(dtoMapper::toExtendedAd);
    }

    public Optional<Ad> createAd(CreateOrUpdateAd createAdDto, String authorEmail) {
        return userRepository.findByEmail(authorEmail)
                .map(author -> {
                    AdEntity adEntity = dtoMapper.toAdEntity(createAdDto);
                    adEntity.setAuthor(author);
                    AdEntity savedAd = adRepository.save(adEntity);
                    return dtoMapper.toAd(savedAd);
                });
    }

    public Optional<Ad> updateAd(Integer id, CreateOrUpdateAd updateAdDto) {
        return adRepository.findById(id)
                .map(existingAd -> {
                    existingAd.setTitle(updateAdDto.getTitle());
                    existingAd.setPrice(updateAdDto.getPrice());
                    existingAd.setDescription(updateAdDto.getDescription());
                    AdEntity updatedAd = adRepository.save(existingAd);
                    return dtoMapper.toAd(updatedAd);
                });
    }

    public boolean deleteAd(Integer id) {
        if (adRepository.existsById(id)) {
            adRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Ads getAdsByAuthor(String authorEmail) {
        return userRepository.findByEmail(authorEmail)
                .map(user -> {
                    List<AdEntity> adEntities = adRepository.findByAuthorId(user.getId());
                    List<Ad> adDtos = dtoMapper.toAdList(adEntities);

                    Ads ads = new Ads();
                    ads.setCount(adDtos.size());
                    ads.setResults(adDtos);
                    return ads;
                })
                .orElseGet(() -> {
                    Ads ads = new Ads();
                    ads.setCount(0);
                    return ads;
                });
    }


}
