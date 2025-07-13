package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.AdvertDTO.AdvertCreateDTO;
import ru.skypro.homework.dto.AdvertDTO.AdvertDTO;
import ru.skypro.homework.dto.AdvertDTO.UserShortDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ads")
public class AdvertController {
    // Временное хранилище объявлений (заглушка)
    private final List<AdvertDTO> stubAdverts = new ArrayList<>();
    private final AtomicLong advertIdCounter = new AtomicLong(1);

    public AdvertController() {
        // Инициализация тестовых данных
        initializeStubData();
    }

    private void initializeStubData() {
        UserShortDTO author = new UserShortDTO();
        author.setId(1L);
        author.setUsername("stubUser");
        author.setAvatar("https://example.com/avatar1.jpg");

        AdvertDTO advert = new AdvertDTO();
        advert.setId(advertIdCounter.getAndIncrement());
        advert.setTitle("Продам ноутбук HP Pavilion");
        advert.setDescription("Состояние отличное, 2022 год выпуска. Полная комплектация.");
        advert.setPrice(45000.0);
        advert.setImages(new String[]{
                "https://example.com/ads/laptop1.jpg",
                "https://example.com/ads/laptop2.jpg"
        });
        advert.setAuthor(author);
        advert.setCreatedAt(LocalDateTime.now().minusDays(2));
        stubAdverts.add(advert);
    }

    /**
     * Заглушка для получения всех объявлений
     * @return Список всех объявлений
     */
    @GetMapping
    public ResponseEntity<List<AdvertDTO>> getAllAdverts() {
        return ResponseEntity.ok(stubAdverts);
    }

    /**
     * Заглушка для получения объявлений текущего пользователя
     * @return Список объявлений пользователя
     */
    @GetMapping("/me")
    public ResponseEntity<List<AdvertDTO>> getUserAdverts() {
        return ResponseEntity.ok(stubAdverts.stream()
                .filter(ad -> ad.getAuthor().getId().equals(1L)) // Фильтр по автору
                .collect(Collectors.toList()));
    }

    /**
     * Заглушка для получения конкретного объявления
     * @param id ID объявления
     * @return Данные объявления
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdvertDTO> getAdvert(@PathVariable Long id) {
        return ResponseEntity.ok(stubAdverts.stream()
                .filter(ad -> ad.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Объявление не найдено")));
    }

    /**
     * Заглушка для создания нового объявления
     * @param createDTO Данные нового объявления
     * @return Созданное объявление
     */
    @PostMapping
    public ResponseEntity<AdvertDTO> createAdvert(@RequestBody AdvertCreateDTO createDTO) {
        UserShortDTO author = new UserShortDTO();
        author.setId(1L);
        author.setUsername("stubUser");
        author.setAvatar("https://example.com/avatar1.jpg");

        AdvertDTO newAdvert = new AdvertDTO();
        newAdvert.setId(advertIdCounter.getAndIncrement());
        newAdvert.setTitle(createDTO.getTitle());
        newAdvert.setDescription(createDTO.getDescription());
        newAdvert.setPrice(createDTO.getPrice());
        newAdvert.setImages(new String[0]); // Пока без изображений
        newAdvert.setAuthor(author);
        newAdvert.setCreatedAt(LocalDateTime.now());
        stubAdverts.add(newAdvert);

        return ResponseEntity.ok(newAdvert);
    }

    /**
     * Заглушка для обновления объявления
     * @param id ID объявления
     * @param updateDTO Новые данные
     * @return Обновленное объявление
     */
    @PatchMapping("/{id}")
    public ResponseEntity<AdvertDTO> updateAdvert(
            @PathVariable Long id,
            @RequestBody AdvertCreateDTO updateDTO) {
        AdvertDTO advert = stubAdverts.stream()
                .filter(ad -> ad.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        advert.setTitle(updateDTO.getTitle());
        advert.setDescription(updateDTO.getDescription());
        advert.setPrice(updateDTO.getPrice());

        return ResponseEntity.ok(advert);
    }

    /**
     * Заглушка для удаления объявления
     * @param id ID удаляемого объявления
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvert(@PathVariable Long id) {
        stubAdverts.removeIf(ad -> ad.getId().equals(id));
        return ResponseEntity.noContent().build();
    }

    /**
     * Заглушка для загрузки изображения к объявлению
     * @param id ID объявления
     * @param file Файл изображения
     * @return URL загруженного изображения
     */
    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadAdvertImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok("https://example.com/ads/" + id + "/" + file.getOriginalFilename());
    }
}
