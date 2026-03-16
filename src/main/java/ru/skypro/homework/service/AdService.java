package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для работы с объявлениями.
 *
 * Содержит бизнес-логику получения, создания,
 * обновления, удаления объявлений и обновления их изображений.
 */
@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    /**
     * Получить список всех объявлений.
     *
     * @return объект Ads со списком объявлений
     */
    public Ads getAllAds() {
        List<Ad> adDtos = adRepository.findAll()
                .stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        Ads ads = new Ads();
        ads.setCount(adDtos.size());
        ads.setResults(adDtos);
        return ads;
    }

    /**
     * Получить расширенную информацию об объявлении по идентификатору.
     *
     * @param id идентификатор объявления
     * @return Optional с расширенной информацией об объявлении
     */
    public Optional<ExtendedAd> getAdById(Integer id) {
        return adRepository.findById(id)
                .map(adMapper::toExtendedDto);
    }

    /**
     * Создать новое объявление.
     *
     * @param dto данные для создания объявления
     * @param email email текущего пользователя
     * @return Optional с созданным объявлением
     */
    public Optional<Ad> addAd(CreateOrUpdateAd dto, String email) {
        Optional<UserEntity> authorOptional = userRepository.findByEmail(email);

        if (authorOptional.isEmpty()) {
            return Optional.empty();
        }

        AdEntity adEntity = adMapper.fromDto(dto, authorOptional.get());
        adEntity.setImage("placeholder.jpg");

        AdEntity savedAd = adRepository.save(adEntity);
        return Optional.of(adMapper.toDto(savedAd));
    }

    /**
     * Обновить объявление.
     *
     * @param id идентификатор объявления
     * @param dto новые данные объявления
     * @param email email текущего пользователя
     * @return Optional с обновленным объявлением
     */
    public Optional<Ad> updateAd(Integer id, CreateOrUpdateAd dto, String email) {
        Optional<AdEntity> adOptional = adRepository.findById(id);
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (adOptional.isEmpty() || userOptional.isEmpty()) {
            return Optional.empty();
        }

        AdEntity adEntity = adOptional.get();
        UserEntity currentUser = userOptional.get();

        if (!canEditAd(adEntity, currentUser)) {
            throw new AccessDeniedException("You cannot edit чужое объявление");
        }

        adMapper.updateAdFields(dto, adEntity);

        AdEntity updatedAd = adRepository.save(adEntity);
        return Optional.of(adMapper.toDto(updatedAd));
    }

    /**
     * Удалить объявление.
     *
     * @param id идентификатор объявления
     * @param email email текущего пользователя
     * @return true, если объявление успешно удалено
     */
    public boolean deleteAd(Integer id, String email) {
        Optional<AdEntity> adOptional = adRepository.findById(id);
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (adOptional.isEmpty() || userOptional.isEmpty()) {
            return false;
        }

        AdEntity adEntity = adOptional.get();
        UserEntity currentUser = userOptional.get();

        if (!canEditAd(adEntity, currentUser)) {
            throw new AccessDeniedException("You cannot delete чужое объявление");
        }

        adRepository.delete(adEntity);
        return true;
    }

    /**
     * Проверить, может ли пользователь редактировать объявление.
     *
     * @param adEntity объявление
     * @param currentUser текущий пользователь
     * @return true, если пользователь является автором объявления или ADMIN
     */
    private boolean canEditAd(AdEntity adEntity, UserEntity currentUser) {
        return adEntity.getAuthor().getId().equals(currentUser.getId())
                || "ADMIN".equals(currentUser.getRole());
    }

    /**
     * Обновить изображение объявления.
     *
     * @param id идентификатор объявления
     * @param imagePath путь к изображению
     * @param email email текущего пользователя
     * @return Optional с обновленным объявлением
     */
    public Optional<Ad> updateAdImage(Integer id, String imagePath, String email) {
        Optional<AdEntity> adOptional = adRepository.findById(id);
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (adOptional.isEmpty() || userOptional.isEmpty()) {
            return Optional.empty();
        }

        AdEntity adEntity = adOptional.get();
        UserEntity currentUser = userOptional.get();

        if (!canEditAd(adEntity, currentUser)) {
            throw new org.springframework.security.access.AccessDeniedException("You cannot update чужое объявление");
        }

        adEntity.setImage(imagePath);
        AdEntity savedAd = adRepository.save(adEntity);

        return Optional.of(adMapper.toDto(savedAd));
    }
}