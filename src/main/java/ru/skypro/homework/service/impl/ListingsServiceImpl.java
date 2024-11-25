package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import ru.skypro.homework.config.GetAuthentication;
import ru.skypro.homework.dto.listing.CreateOrUpdateListing;
import ru.skypro.homework.dto.listing.ExtendedListingDTO;
import ru.skypro.homework.dto.listing.ListingDTO;
import ru.skypro.homework.dto.listing.ListingsDTO;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.Listing;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.ListingMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.ListingRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.ListingsService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * <b>Сервис для работы с объявлениями (Listings). </b> <p>
 *
 * <p>Этот класс реализует методы для получения списка объявлений текущего пользователя,
 * обновления изображений объявлений и проверки прав доступа к объявлениям.</p>
 *
 * <p>Сервис использует репозиторий для взаимодействия с базой данных и маппер для преобразования
 * объектов между слоями приложения.</p>
 */
@Service
@RequiredArgsConstructor
public class ListingsServiceImpl implements ListingsService {

    private final ListingRepository listingRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final ListingMapper listingMapper;
    private final ImageService imageService;

    /**
     * <b>Метод получения всех объявлений. </b> <p>
     * (В виде объекта DTO {@link ListingDTO} )
     */
    @Override
    public ListingsDTO getAllListings() {
        List<Listing> listings = listingRepository.findAll();
        return listingMapper.listingListToListings(listings);
    }

    /**
     * <b>Метод добавления объявления в БД. </b> <p>
     * Принцип работы:<p>
     * На основе минимальной информации DTO {@link CreateOrUpdateListing}
     * создать полноценный объект объявления {@link Listing},
     * на основе данных аутентификации создать объект пользователя {@link User},
     * добавить созданного пользователя и картинку из параметра в объект объявления,
     * сохранить получившийся объект с помощью репозитория {@link ListingRepository},
     *
     * @param  createOrUpdateListing DTO объявления {@link CreateOrUpdateListing} <p>
     * @param  image картинка товара {@link MultipartFile} <p>
     * @param  authentication переменная аутентификации хранящая данные об авторизованном пользователе {@link Authentication} <p>
     */
    @Override
    public ListingDTO addListing(CreateOrUpdateListing createOrUpdateListing, MultipartFile image, Authentication authentication) throws IOException {
        Listing listing = listingMapper.createOrUpdateListingToListing(createOrUpdateListing);
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        listing.setAuthor(user);
        listingRepository.save(listing);
        listing.setImage(imageService.uploadImage(listing.getId(), image));
        listingRepository.save(listing);
        return listingMapper.listingToListingDTO(listing);
    }

    /**
     * <b>Метод получения объявления по ID </b> <p>
     * Запрошенный {@link Listing} преобразуется в DTO {@link ExtendedListingDTO} и возвращается методом.
     */
    @Override
    public ExtendedListingDTO getListing(long id) {
        return listingMapper.toExtendedListing(listingRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объявление с ID = " + id + " не найдено ")));
    }

    /**
     * <b> Метод удаления объявления. </b> <p>
     * Метод использует аннотацию {@link Transactional} <p>
     * Принцип работы:<p>
     * 1) По переданному ID находится объявление <p>
     * 2) Проводится проверка на доступ к редактированию объявления
     * через метод {@link #checkPermit(Listing, Authentication)} <p>
     * 3) В репозиториях {@link CommentRepository} , {@link ImageRepository} , {@link ListingRepository} - вызываются методы удаления
     * комментариев объявления, картинки объявления, и данных об объявлении соответственно.
     *
     * @param id id объявления (long)
     * @param authentication объект аутентификации с данными текущего пользователя
     */
    @Override
    @Transactional
    public void deleteListing(long id, Authentication authentication) throws AccessDeniedException {

        Listing listing = listingRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объявление с ID" + id + "не найдено"));

        checkPermit(listing, authentication);
        commentRepository.deleteCommentsByListingId(id);
        imageRepository.deleteById(listing.getImage().getId());
        listingRepository.deleteById(id);
    }


    /**
     * <b> Метод изменения данных объявления. </b> <p>
     * (Названия, описания и цены)  {@link CreateOrUpdateListing} <p>
     * Принцип работы:<p>
     * Находит по ID объявление, уточняет есть ли у пользователя доступ к редактированию {@link #checkPermit(Listing, Authentication)} ,
     *  далее меняет данные (Названия, описания и цены), сохраняет объявление, возвращает DTO только что измененного
     *  объекта {@link ListingDTO}
     * @param id id объявления (long)
     * @param createOrUpdateListing DTO объявления {@link CreateOrUpdateListing}
     * @param authentication объект аутентификации с данными текущего пользователя
     * @return {@link ListingDTO} DTO объявления
     */
    @Override
    public ListingDTO updateListing(long id, CreateOrUpdateListing createOrUpdateListing, Authentication authentication) throws AccessDeniedException {
        Listing listing = listingRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объявление с ID" + id + "не найдено"));
        checkPermit(listing, authentication);
        listing.setTitle(createOrUpdateListing.getTitle());
        listing.setDescription(createOrUpdateListing.getDescription());
        listing.setPrice(createOrUpdateListing.getPrice());
        listingRepository.save(listing);
        return listingMapper.listingToListingDTO(listing);
    }


    /**
     * <b> Метод возвращающий все объявления пользователя. </b> <p>
     * Принцип работы:<p>
     * Из {@link Authentication} достает пользователя, по данному пользователю находим все его объявления.
     * Маппер пакует лист объявлений в  {@link ListingsDTO} и возвращает DTO
     * @param authentication объект аутентификации с данными текущего пользователя
     * @return {@link ListingsDTO} DTO всех объявлений пользователя
     */
    @Override
    public ListingsDTO getListingsMe(Authentication authentication) {
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        List<Listing> listingList = listingRepository.findListingByAuthorId(user.getId());
        return listingMapper.listingListToListings(listingList);
    }

    /**
     * <b> Метод изменения изображения объявления. </b> <p>
     * Метод использует аннотацию {@link Transactional} <p>
     * Принцип работы:<p>
     * Найти объявление по ID, далее проверить доступ к редактированию, из объявления взять текущую картинку,
     * сохранить её отдельно, на её место сохранить новую, не актуальную картинку - удалить, сохранить объект объявления,
     * в котором уже будет новая картинка.
     * @param id id объявления (long)
     * @param image файл  {@link MultipartFile} (картинка объявления)
     * @param authentication объект аутентификации с данными текущего пользователя
     * @throws IOException (может выкинуть ошибки загрузки)
     */
    @Override
    @Transactional
    public void updateListingImage(Long id, MultipartFile image, Authentication authentication) throws IOException {
        Listing listing = listingRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объявление с ID" + id + " не найдено"));
        checkPermit(listing, authentication);
        Image imageFile = listing.getImage();
        listing.setImage(imageService.uploadImage(listing.getId(), image));
        imageService.removeImage(imageFile);
        listingRepository.save(listing);
    }

    /**
     * <b> Метод проверки доступа к редактированию объявления. </b> <p>
     * Служебный внутренний метод принимающий на вход: <p> {@link Listing} и {@link Authentication} <p>
     * далее сравнивает автора объявления и текущего пользователя, а также проверяет, является ли пользователь Админом.
     * Если текущий пользователь не автор объявления и не админ, то будет выброшено  {@link AccessDeniedException}
     *
     * @param listing объявление
     * @param authentication объект аутентификации с данными текущего пользователя
     */
    public void checkPermit(Listing listing, Authentication authentication) throws AccessDeniedException {
        if (!listing.getAuthor().getEmail().equals(authentication.getName())
                && !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new AccessDeniedException("Вы не можете редактировать или удалять чужое объявление");
        }
    }


}