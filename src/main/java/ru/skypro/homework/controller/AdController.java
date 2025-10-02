package ru.skypro.homework.controller;


import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ads")

public class AdController {

    @GetMapping
    public List<AdDto> getAllAds() {
        // Логика для получения всех объявлений
        return new ArrayList<>(); // Возвращаем список объявлений
    }

    @PostMapping
    public AdDto addAd(@RequestBody AdDto adDto) {
        // Логика для добавления нового объявления
        return adDto; // Возвращаем добавленное объявление
    }

    @GetMapping("/{id}")
    public AdDto getAdById(@PathVariable Integer id) {
        // Логика для получения объявления по ID
        return new AdDto(); // Возвращаем объявление по ID
    }

    @PatchMapping("/{id}")
    public AdDto updateAd(@PathVariable Integer id, @RequestBody AdDto adDto) {
        // Логика для обновления объявления
        return adDto; // Возвращаем обновленное объявление
    }

    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable Integer id) {
        // Логика для удаления объявления
    }
}

