package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdExtendedDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.map.AdMap;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.function.Supplier;

@Service
@Slf4j
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final AdMap adMap;
    private final UserRepository userRepository;

    public AdServiceImpl(AdRepository adRepository, AdMap adMap, UserRepository userRepository) {
        this.adRepository = adRepository;
        this.adMap = adMap;
        this.userRepository = userRepository;
    }

    /**
     * Получение всех объявлений
     *
     * @return = получаем лист объявлений
     */
    @Override

    public AdsDto getAllAds() {
        return adMap.mapAdsDto(adRepository.findAll());
    }

    /**
     * Записывает в базу новое объявление
     *
     * @param createOrUpdateAdDto информация о новом объявлении
     * @param image               часть запроса, содержащая картинку
     * @param principal           принципал, чье имя используется для идентификации автора
     * @return сохраненное объявление
     */
    @Override
    public AdDto addAd(CreateOrUpdateAdDto createOrUpdateAdDto, MultipartFile image, Principal principal) {
        Ad ad = adMap.toEntity(createOrUpdateAdDto, principal);
        try {
            ad.setImage(image.getBytes());
        } catch (IOException e) {
            log.info("Ошибка с записью изображения");
        }
        Ad adAfterSave = adRepository.save(ad);
        return adMap.mapAdDto(adAfterSave);
    }

    /**
     * Получаем объявление по id
     *
     * @param id = id объявления
     * @return = возвращает AdExtendedDto
     */
    @Override
    public AdExtendedDto getAdExtended(int id) {
        return adMap.mapAdExtendedDto(adRepository.findById(id).orElseThrow(excSuppl(id)));
    }

    /**
     * Удаляем объявление по id
     *
     * @param id = id объявления
     */
    @Override
    public void DeleteAd(int id) {
        adRepository.deleteById(id);
    }

    /**
     * Обновление объявления
     *
     * @param id = id объявления
     * @return = AdDto
     */
    @Override
    public AdDto updateAd(int id, CreateOrUpdateAdDto createOrUpdateAdDto) {
        Ad ad = adRepository.findById(id).orElseThrow(excSuppl(id));
        ad.setTitle(createOrUpdateAdDto.getTitle());
        ad.setPrice(createOrUpdateAdDto.getPrice());
        ad.setDescription(createOrUpdateAdDto.getDescription());
        return adMap.mapAdDto(adRepository.save(ad));
    }

    /**
     * Извлекает из базы объявления пользователя
     *
     * @param principal принципал, чье имя используется для идентификации автора
     * @return список объявлений
     */
    @Transactional
    @Override
    public AdsDto getMyAds(Principal principal) {
        String userName = principal.getName();
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));
        List<Ad> adList = user.getAds();
        return adMap.mapAdsDto(adList);
    }

    /**
     * Обновление картинки объявления
     *
     * @param id    = id объявления
     * @param image = путь к file картинки
     */
    @Override
    public void patchImage(int id, MultipartFile image) {
        Ad ad = adRepository.findById(id).orElseThrow(excSuppl(id));
        try {
            ad.setImage(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        adRepository.save(ad);
    }

    private Supplier<EntityNotFoundException> excSuppl(int id) {
        return () -> new EntityNotFoundException("Ad with id " + id + " not found");
    }

    public byte[] getImage(int id) {
        return adRepository.findById(id).orElseThrow().getImage();
    }
}
