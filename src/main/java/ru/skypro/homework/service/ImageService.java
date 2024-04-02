package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ImageService {
    void transferImageToResponse(Integer id, HttpServletResponse response);

    /**
     * @param imageFile файл с изображением
     * @return файл с изображением в директории
     * @throws IOException
     */
    Image saveImageFile(MultipartFile imageFile) throws IOException;
}
