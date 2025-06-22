package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.Exception.ImageNotFoundException;
import ru.skypro.homework.dto.ImageDto;
import ru.skypro.homework.mapper.ImageMapper;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final UserRepository userRepository;

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public Image createImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Image updateImage(Integer id, Image imageDetails) {
        Optional<Image> existingImage = imageRepository.findById(id);
        if (existingImage.isPresent()) {
            Image imageToUpdate = existingImage.get();
            imageToUpdate.setImageUrl(imageDetails.getImageUrl());
            return imageRepository.save(imageToUpdate);
        }
        throw new ImageNotFoundException("Изображение не найдено по id "+id);
    }

    @Override
    public void deleteImage(Integer id) {
        if (!imageRepository.existsById(id)) {
            throw new EntityNotFoundException("Изображение с ID " + id + " не найдено.");
        }
        imageRepository.deleteById(id);
    }

    @Override
    public Image saveImage(Integer id, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл не должен быть пустым");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));

        Image image = new Image();
        image.setUser(user);
        image.setData(file.getBytes());

        return imageRepository.save(image);
    }

    @Override
    public void saveToDatabase(ImageDto imageDto, Path imagePath, MultipartFile imageFile) {
        Optional<Image> optionalImage = imageRepository.findByUserId(imageDto.getUserId());

        Image image = optionalImage.orElseGet(() -> {
            Image newImage = new Image();
            User user = userRepository.findById(imageDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + imageDto.getUserId() + " не найден"));
            newImage.setUser(user);
            return newImage;
        });


        image.setImageUrl(imagePath.toString());
        try {
            image.setData(imageFile.getBytes());
        } catch (IOException e) {
            log.error("Error reading file for user {}: {}", imageDto.getUserId(), e.getMessage());
            throw new RuntimeException("Ошибка при чтении файла. Попробуйте снова.", e);
        }
        imageRepository.save(image);
    }

    @Override
    public Optional<Image> findImageById(Integer id) {
        return imageRepository.findById(id);
    }
}
