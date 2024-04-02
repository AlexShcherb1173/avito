package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserOrAdminService;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.UserNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdsServiceImpl implements AdsService {
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final  UserOrAdminService userOrAdminService;
    private final AdsMapper adsMapper;

    /**
     * @return список объявлений
     */
    @Override
    public List<Ads> getAllAds() {
        List<Ads> adsList = adsRepository.findAll();
        log.info(adsList.toString());
        return adsList;

    }

    /**
     * @param createOrUpdateAdDto заголовок, цена и описание
     * @param email электронная почта
     * @param imageFile файл с изображением
     * @return объявление
     */
    @Override
    public Ads saveAd(CreateOrUpdateAdDto createOrUpdateAdDto, String email, MultipartFile imageFile)  {
        log.info("запустился метод сохранения объявления");
        try {
        Ads saveAds = adsMapper.createOrUpdateAdDtoToAds(createOrUpdateAdDto);
        saveAds.setUser(userRepository.findUserByEmail(email).orElseThrow());
        Image image = imageService.saveImageFile(imageFile);
        saveAds.setImage(image);
        adsRepository.saveAndFlush(saveAds);
        return saveAds;
        } catch (IOException e){
            log.error("Ошибка при сохранении объявления", e);
            throw new AdsNotFoundException("Ошибка при сохранении объявления", e);
        }
    }

    /**
     * @param id id объявления
     * @return объявление
     */
    @Override
    public Ads getAd(Integer id) {
        Ads ads = adsRepository.findById(id).orElseThrow();
        log.info(String.valueOf(ads));
        return ads;

    }

    /**
     * @param email email
     * @param id id объявления
     * @return
     * удаление объявления
     */
    @Override
    public boolean removeAd(String email, Integer id) {
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        User adOwner = ads.getUser();
        if (userOrAdminService.isUserOrAdmin(email, adOwner)) {
            log.info("запустился метод удаления объявления");
            try {
                Files.deleteIfExists(Path.of(ads.getImage().getFilePath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            adsRepository.deleteById(id);
            return true;
        }
        return false;

    }

    /**
     * @param id id объявления
     * @param createOrUpdateAdDto заголовок,цена, описание
     * @param name email
     * @return объявление
     */
    @Override
    public Ads updateAds(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto, String name) {
        Optional<Ads> optionalAds = adsRepository.findById(id);
        if (optionalAds.isPresent()) { //TODO можно заменить на orElseThrow вместо return null
            Ads ads = optionalAds.get(); //TODO убрать из метода
            User adOwner = ads.getUser();
            if (userOrAdminService.isUserOrAdmin(name, adOwner)) {
                log.info("запустился метод обновления объявления");
                adsMapper.updateAdsFromCreateOrUpdateAdDto(createOrUpdateAdDto, ads);
                adsRepository.save(ads);
                return ads;
            }
        }
        return null;
    }
    /**
     * Получает данные об объявлениях пользователя
     *
     * @param authentication данные о текущем пользователе {@link Authentication}
     * @return данные об объявлениях пользователя в виде дто-объекта {@link AdsDto}
     */
    @Override
    public AdsDto getAllUserAds(Authentication authentication) {
        User authenticatedUser = userRepository
                .findUserByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        log.info("Пользователь авторизован и запустился метод getAdsMe!");
        List<Ads> adsList = adsRepository.findAllByUser(authenticatedUser);
        return adsMapper.listAdsToAdsDto(adsList.size(), adsList);
    }

    /**
     * Обновляет картинку объявления
     *
     * @param id        идентификатор объявления
     * @param imageFile новая картинка
     * @return <code>true</code> если картинка обновлена, <code>false</code> в случае неудачи
     */
    @Override
    public boolean updateImageAd(int id, MultipartFile imageFile, String email) {
        log.info("запустился метод updateImage.");
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        User adOwner = ads.getUser();
        if (userOrAdminService.isUserOrAdmin(email, adOwner)) {
            Image image;
            try {
                image = imageService.saveImageFile(imageFile);
            } catch (IOException e) {
                throw new RuntimeException(e); //TODO заменить exception вместо RuntimeException
            }
            ads.setImage(image);
            adsRepository.saveAndFlush(ads);
            return true;
        }
        return false;
    }
}
