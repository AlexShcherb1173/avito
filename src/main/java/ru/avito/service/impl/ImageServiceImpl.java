package ru.avito.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.avito.exception.FileStorageException;
import ru.avito.service.ImageService;
import ru.avito.util.FileTypeUtils;
import ru.avito.util.ImagePathUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public String saveAdImage(Integer adId, MultipartFile file) {
        FileTypeUtils.validateImage(file);

        String extension = FileTypeUtils.resolveExtension(file);
        String filename = "image" + extension;

        Path directory = ImagePathUtils.getAdsDirectory(adId);
        Path targetFile = directory.resolve(filename);

        saveFile(directory, targetFile, file);

        return ImagePathUtils.buildAdImageUrl(adId, filename);
    }

    @Override
    public String saveUserImage(Integer userId, MultipartFile file) {
        FileTypeUtils.validateImage(file);

        String extension = FileTypeUtils.resolveExtension(file);
        String filename = "avatar" + extension;

        Path directory = ImagePathUtils.getUsersDirectory(userId);
        Path targetFile = directory.resolve(filename);

        saveFile(directory, targetFile, file);

        return ImagePathUtils.buildUserImageUrl(userId, filename);
    }

    @Override
    public void deleteImageIfExists(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return;
        }

        try {
            Path path = ImagePathUtils.resolvePhysicalPathFromImageUrl(imagePath);
            if (path != null && Files.exists(path)) {
                Files.delete(path);

                Path parent = path.getParent();
                if (parent != null && Files.exists(parent) && isDirectoryEmpty(parent)) {
                    Files.delete(parent);
                }
            }
        } catch (IOException e) {
            throw new FileStorageException("Failed to delete image: " + imagePath, e);
        }
    }

    private void saveFile(Path directory, Path targetFile, MultipartFile file) {
        try {
            recreateDirectory(directory);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Failed to save image", e);
        }
    }

    private void recreateDirectory(Path directory) throws IOException {
        if (Files.exists(directory)) {
            try (Stream<Path> walk = Files.walk(directory)) {
                walk.sorted(Comparator.reverseOrder())
                        .filter(path -> !path.equals(directory))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            } catch (RuntimeException e) {
                if (e.getCause() instanceof IOException) {
                    throw (IOException) e.getCause();
                }
                throw e;
            }
        }

        Files.createDirectories(directory);
    }

    private boolean isDirectoryEmpty(Path directory) throws IOException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
    }
}