package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAdsDto;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.*;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.dto.Role;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AdsService {

    private final AdsRepository adsRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final AdsMapper mapper;
    private final AdsListMapper listMapper;

    /**
     * Получение из базы данных объявления с указанным id.
     */
    public Ads getAdById(Integer id) {
        return adsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ad with id=" + id + " not found"));
    }

    /**
     * Изменение изображения объявления.
     *
     * @param adId      id объявления.
     * @param imageFile новое изображение.
     * @return новое изображение в формате byte[].
     */
    public byte[] editAdImage(Integer adId, MultipartFile imageFile) throws IOException {
        Ads ad = getAdById(adId);
        Image oldImage = ad.getImage();
        ad.setImage(imageService.uploadImage(imageFile));
        adsRepository.save(ad);
        imageService.deleteImage(oldImage);
        return imageFile.getBytes();
    }

    /**
     * Создание нового объявления.
     *
     * @param createAdsDto dto с данными для создания объявления.
     * @param imageFile    картинка объявления.
     * @param username     имя пользователя из аутентификации.
     */
    public AdsDto createAd(CreateAdsDto createAdsDto, MultipartFile imageFile, String username) throws IOException {
        Ads ad = mapper.toAds(createAdsDto);
        User author = userService.getUserByUsername(username);
        ad.setAuthor(author);
        ad.setImage(imageService.uploadImage(imageFile));
        return mapper.toDto(adsRepository.save(ad));
    }

    /**
     * Поиск объявления по части названия.
     *
     * @param titlePart часть названия для поиска.
     * @return Список объявлений в формате ResponseWrapperAdsDto.
     */
    public ResponseWrapperAdsDto getAdsByTitlePart(String titlePart) {
        return listMapper.toResponseWrapperAdsDto(adsRepository.findByTitleContainingIgnoreCase(titlePart));
    }

    /**
     * Получение списка всех объявлений.
     *
     * @return Список объявлений в формате ResponseWrapperAdsDto.
     */
    public ResponseWrapperAdsDto getAdsAll() {
        List<Ads> ads = adsRepository.findAll();
        return listMapper.toResponseWrapperAdsDto(ads);
    }

    /**
     * Получение расширенной информации об объявлении.
     */
    public FullAdsDto getAdInfo(Integer id) {
        return mapper.toFullAdsDto(getAdById(id));
    }

    /**
     * Удаление объявления из базы данных.
     *
     * @param id       id объявления.
     * @param username имя пользователя из аутентификации.
     * @return true - если удаление прошло успешно, false - если удаление было запрещено из-за недостатка прав.
     */
    public boolean deleteAd(Integer id, String username) throws IOException {
        User user = userService.getUserByUsername(username);
        Ads ad = getAdById(id);
        if (user.equals(ad.getAuthor()) || user.getRole() == Role.ADMIN) {
            Image image = ad.getImage();
            commentRepository.deleteAll(ad.getCommentsList());
            adsRepository.delete(ad);
            imageService.deleteImage(image);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Обновление объявления в базе данных.
     *
     * @param id           id объявления.
     * @param createAdsDto dto с данными для обновления.
     * @param username     имя пользователя из аутентификации.
     * @return Обновлённое объявление в формате AdsDto, если операция прошла успешно,
     * или пустой Optional, если обновление было запрещено из-за недостатка прав.
     */
    public Optional<AdsDto> updateAd(Integer id, CreateAdsDto createAdsDto, String username) {
        User user = userService.getUserByUsername(username);
        Ads ad = getAdById(id);
        if (user.equals(ad.getAuthor()) || user.getRole() == Role.ADMIN) {
            mapper.updateAds(createAdsDto, ad);
            return Optional.of(mapper.toDto(adsRepository.save(ad)));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Получение списка объявлений конкретного пользователя.
     *
     * @param username имя пользователя из аутентификации.
     * @return Список объявлений в формате ResponseWrapperAdsDto.
     */
    public ResponseWrapperAdsDto getAdsMe(String username) {
        List<Ads> ads = adsRepository.findByAuthor_UserName(username);
        return listMapper.toResponseWrapperAdsDto(ads);
    }

}