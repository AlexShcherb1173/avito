package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public Image uploadImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setFileSize(file.getSize());
        image.setMediaType(file.getContentType());
        image.setData(generateDataForDB(file));
        return imageRepository.save(image);
    }

    @Override
    public Image getImage(int id) {
        return imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
    }

    @Override
    public void deleteImage(int id) {
        imageRepository.deleteById(id);
    }

    private String getExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    private byte[] generateDataForDB(MultipartFile file) throws IOException {
        try (
                InputStream is = file.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is, 2048);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()
                ) {
            BufferedImage image = ImageIO.read(bis);
            BufferedImage preview = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            graphics2D.dispose();

            ImageIO.write(preview, getExtension(Objects.requireNonNull(file.getOriginalFilename())), baos);
            return baos.toByteArray();
        }
    }
}
