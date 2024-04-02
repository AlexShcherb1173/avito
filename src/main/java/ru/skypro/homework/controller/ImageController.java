package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.service.ImageService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor

public class ImageController {
    private final ImageService imageService;

    /**
     * @param id
     * @param response
     */
    @GetMapping("/image/{id}")
    public void transferImageToResponse(@PathVariable Integer id, HttpServletResponse response) {
       try {
           imageService.transferImageToResponse(id, response);
       } catch(Exception e){
           log.error("Ошибка при передаче изображения в ответ", e);
           throw new ImageNotFoundException("Ошибка при передаче изображения в ответ", e);
       }
    }
}
