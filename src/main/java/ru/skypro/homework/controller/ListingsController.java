package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.listing.CreateOrUpdateListing;
import ru.skypro.homework.dto.listing.ExtendedListingDTO;
import ru.skypro.homework.dto.listing.ListingDTO;
import ru.skypro.homework.dto.listing.ListingsDTO;
import ru.skypro.homework.service.impl.ImageServiceImpl;
import ru.skypro.homework.service.impl.ListingsServiceImpl;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * <b>Контроллер для обработки запросов, связанных с объявлениями. </b> <p>
 *
 * <p>Класс {@link ListingsController} предоставляет API для получения, добавления, удаления и обновления объявлений.</p>
 *
 * <p>Обрабатывает следующие запросы:</p>
 * <ul>
 *     <li><code>GET /ads</code> - получение всех объявлений.</li>
 *     <li><code>POST /ads</code> - добавление нового объявления.</li>
 *     <li><code>GET /ads/{id}</code> - получение информации об объявлении по его ID.</li>
 *     <li><code>DELETE /ads/{id}</code> - удаление объявления по его ID.</li>
 *     <li><code>PATCH /ads/{id}</code> - обновление информации об объявлении.</li>
 *     <li><code>GET /ads/me</code> - получение объявлений авторизованного пользователя.</li>
 * </ul>
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class ListingsController {

    private final ListingsServiceImpl listingService;
    private final ImageServiceImpl imageService;

    /**
     * <b>Метод для получения всех объявлений. </b> <p>
     *
     * @return ResponseEntity с объектом {@link ListingsDTO}, содержащим список всех объявлений.
     */
    @GetMapping()
    @Operation(summary = "Получение всех объявлений", description = "getAllListings")
    public ResponseEntity<ListingsDTO> getAllListings() {
        return ResponseEntity.ok(listingService.getAllListings());
    }

    /**
     * <b>Метод для добавления нового объявления. </b> <p>
     *
     * @param createOrUpdateListingDTO Объект, содержащий данные нового объявления.
     * @param image Файл изображения для объявления.
     * @param authentication Информация о текущем аутентифицированном пользователе.
     * @return ResponseEntity с объектом {@link ListingDTO}, содержащим добавленное объявление.
     * @throws IOException если произошла ошибка при обработке изображения.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавление объявления", description = "addListing")
    public ResponseEntity<ListingDTO> addListing(@RequestPart(value = "properties", required = false) CreateOrUpdateListing createOrUpdateListingDTO,
                                                 @RequestPart("image") MultipartFile image,
                                                 Authentication authentication) throws IOException {
        return ResponseEntity.ok(listingService.addListing(createOrUpdateListingDTO, image, authentication));
    }

    /**
     * <b>Метод для получения информации об объявлении по его ID. </b> <p>
     *
     * @param id ID объявления.
     * @return ResponseEntity с объектом {@link ExtendedListingDTO}, содержащим информацию об объявлении.
     */
    @GetMapping("{id}")
    @Operation(summary = "Получение информации об объявлении", description = "getListing")
    public ResponseEntity<ExtendedListingDTO> getListing(@PathVariable long id) {
        return ResponseEntity.ok(listingService.getListing(id));
    }

    /**
     * <b>Метод для удаления объявления по его ID. </b> <p>
     *
     * @param id ID объявления, которое необходимо удалить.
     * @param authentication Информация о текущем аутентифицированном пользователе.
     * @throws AccessDeniedException если у пользователя нет прав на удаление объявления.
     * @return ResponseEntity с HTTP статусом 204 (NO CONTENT) в случае успешного удаления.
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Удаление объявления", description = "removeListing")
    public ResponseEntity<?> deleteListing(@Parameter(description = "ID объявления")
                                               @PathVariable long id, Authentication authentication) throws AccessDeniedException {
        listingService.deleteListing(id, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * <b>Метод для обновления информации об объявлении. </b> <p>
     *
     * @param id ID объявления, которое необходимо обновить.
     * @param createOrUpdateListing Объект, содержащий новые данные для объявления.
     * @param authentication Информация о текущем аутентифицированном пользователе.
     * @throws AccessDeniedException если у пользователя нет прав на обновление объявления.
     * @return ResponseEntity с объектом {@link ListingDTO}, содержащим обновленное объявление.
     */
    @PatchMapping("{id}")
    @Operation(summary = "Обновление информации об объявлении", description = "updateInfoListing")
    public ResponseEntity<ListingDTO> updateListingInformation(@Parameter(description = "ID объявления") @PathVariable long id,
                                                               @RequestBody CreateOrUpdateListing createOrUpdateListing,
                                                               Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok(listingService.updateListing(id, createOrUpdateListing, authentication));
    }

    /**
     * <b>Метод для получения объявлений авторизованного пользователя. </b> <p>
     *
     * @param authentication Информация о текущем аутентифицированном пользователе.
     * @return ResponseEntity с объектом {@link ListingsDTO}, содержащим список объявлений пользователя.
     */
    @GetMapping("/me")
    @Operation(summary = "Получение объявлений авторизованного пользователя", description = "getListingsMe")
    public ResponseEntity<ListingsDTO> getListingsFromAuthorizedUser(Authentication authentication) {
        return ResponseEntity.ok(listingService.getListingsMe(authentication));
    }

    /**
     * <b>Обновляет изображение для указанного объявления. </b> <p>
     *
     * @param id идентификатор объявления, для которого необходимо обновить изображение
     * @param image файл изображения, который будет загружен
     * @param authentication объект аутентификации текущего пользователя
     * @return ResponseEntity с HTTP статусом 200 (OK) при успешном обновлении изображения
     * @throws IOException если произошла ошибка при обработке файла изображения
     */
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление картинки объявления", description = "updateImage")
    public ResponseEntity<?> updateImage(@Parameter(description = "ID объявления") @PathVariable("id") long id,
                                         @RequestPart("image") MultipartFile image, Authentication authentication) throws IOException {
        listingService.updateListingImage(id, image, authentication);
        return ResponseEntity.ok().build();
    }

    /**
     * <b>Получает изображение для указанного объявления. </b> <p>
     *
     * @param id идентификатор объявления, для которого необходимо получить изображение
     * @return ResponseEntity с байтовым массивом изображения и соответствующим HTTP статусом
     */
    @GetMapping(value = "/image/{id}", produces = {MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE
    })
    @Operation(summary = "Получение картинки объявления", description = "getListingsImage")
    public ResponseEntity<byte[]> getListingsImage(@PathVariable("id") long id) {
        byte[] image = imageService.getImage(id).getData();
        return ResponseEntity.ok(image);
    }
}
