package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.entity.Picture;
import ru.skypro.homework.service.impl.PictureServiceImpl;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Картинки", description = "Метод для работы с картинками")
public class PictureController implements PictureControllerInterface {
    private final PictureServiceImpl pictureServiceImpl;

    public PictureController(PictureServiceImpl pictureServiceImpl) {
        this.pictureServiceImpl = pictureServiceImpl;
    }

    @GetMapping(value = "/pictures/{id}")
    @Override
    public byte[] getImage(@PathVariable Integer id) {
        Picture picture = pictureServiceImpl.findById(id);
        return picture.getData();
    }
}
