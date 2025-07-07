package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.ObjectNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.ExtendedAdMapper;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImagesService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Service
public class AdsServiceImpl implements AdsService {

    Logger logger = LoggerFactory.getLogger(AdsServiceImpl.class);
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final UserDetailsManager userDetailsManager;
    private final ImagesService imagesService;
    private final ExtendedAdMapper extendedAdMapper;

    private final AdMapper adMapper;

    @Value("${ads.image-file-dir}")
    private String imageFileDir;

    public AdsServiceImpl(AdsRepository adsRepository, UserRepository userRepository, UserDetailsManager userDetailsManager,  ImagesService imagesService,  AdMapper adMapper, ExtendedAdMapper extendedAdMapper) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.userDetailsManager = userDetailsManager;
        this.imagesService = imagesService;
        this.adMapper = adMapper;
        this.extendedAdMapper = extendedAdMapper;
    }

    // Получение всех объявлений
    @Override
    public Ads getAllAds() {
        List<AdModel> ads = adsRepository.findAll();
        return adMapper.makeAds(ads);
    }

    // Добавление объявления
    @Override
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image) throws IOException {
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AdModel adModel = new AdModel();
        adModel.setTitle(properties.getTitle());
        adModel.setDescription(properties.getDescription());
        adModel.setPrice(properties.getPrice());
        adModel.setAuthor(userModel);

        adsRepository.save(adModel);

        // обрабатываем полученный файл
        String targetFileName = imagesService.makeTargetFileName(Objects.requireNonNull(image.getOriginalFilename()), adModel.getId(), FileTypeEnum.PRODUCT);
        imagesService.saveFileToDisk(image, targetFileName);
        adModel.setImage(targetFileName);
        adsRepository.save(adModel);

        return adMapper.makeAd(adModel);
    }

    // Получение информации об объявлении
    @Override
    public ExtendedAd getExtendedAdById(long id) throws ObjectNotFoundException {
        AdModel adModel = adsRepository.findById(id).orElse(null);

        if (adModel == null) {
            throw new ObjectNotFoundException();
        }

        return extendedAdMapper.makeExtendedAd(adModel);
    }

    // Удаление объявления
    @Override
    public void removeAd(long id) throws ForbiddenException, ObjectNotFoundException {
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Находим удаляемое объявление в БД по его ID
        AdModel adModel = adsRepository.findById(id).orElse(null);

        if (adModel == null) {
            // объявление не найдено - ошибка 404
            throw new ObjectNotFoundException();
        }

        // Проверяем, принадлежит ли удаляемое объявление текущему пользователю
        long adUserId = adModel.getAuthor().getId();
        if (adUserId != userModel.getId() && !userModel.getRole().equals(Role.ADMIN.toString())) {
            // не принадлежит - ошибка 403
            throw new ForbiddenException();
        }

        adsRepository.delete(adModel);
    }

    // Обновление информации об объявлении
    @Override
    public Ad updateAds(long id, CreateOrUpdateAd createOrUpdateAd) throws ForbiddenException, ObjectNotFoundException {
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Ищем объявление
        AdModel adModel = adsRepository.findById(id).orElse(null);
        if (adModel == null) {
            throw new ObjectNotFoundException();
        }

        if (adModel.getAuthor().getId() != userModel.getId() || !userModel.getRole().equals("ADMIN")) {
            // Если текущий пользователь не автор этого объявления или не админ, то запрещаем редактирование
            throw new ForbiddenException();
        }

        adModel.setTitle(createOrUpdateAd.getTitle());
        adModel.setDescription(createOrUpdateAd.getDescription());
        adModel.setPrice(createOrUpdateAd.getPrice());

        adsRepository.save(adModel);

        return adMapper.makeAd(adModel);
    }

    // Получение объявлений авторизованного пользователя
    @Override
    public Ads getAdsMe() {
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<AdModel> ads = adsRepository.findByAuthorId(userModel.getId());
        return adMapper.makeAds(ads);
    }

    // Обновление картинки объявления
    @Override
    public byte[] updateImage(long id, MultipartFile image) throws IOException, ObjectNotFoundException, ForbiddenException {
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Ищем объявление
        AdModel adModel = adsRepository.findById(id).orElse(null);
        if (adModel == null) {
            throw new ObjectNotFoundException();
        }

        if (adModel.getAuthor().getId() != userModel.getId() || !userModel.getRole().equals("ADMIN")) {
            // Если текущий пользователь не автор этого объявления или не админ, то запрещаем редактирование
            throw new ForbiddenException();
        }

        String targetFileName = imagesService.makeTargetFileName(Objects.requireNonNull(image.getOriginalFilename()), adModel.getId(), FileTypeEnum.PRODUCT);

        imagesService.saveFileToDisk(image, targetFileName);
        adModel.setImage(targetFileName);
        adsRepository.save(adModel);

        Path filePath = Path.of(imageFileDir, targetFileName);
        return Files.readAllBytes(filePath);
    }
}
