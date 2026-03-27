package ru.avito.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.avito.exception.FileStorageException;
import ru.avito.service.ImageService;
import ru.avito.util.FileTypeUtils;
import ru.avito.util.ImagePathUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImagePathUtils imagePathUtils;

    @Override
    public String saveAdImage(Integer adId, MultipartFile file) {
        FileTypeUtils.validateImage(file);

        String extension = FileTypeUtils.resolveExtension(file);
        String filename = "image" + extension;

        Path directory = imagePathUtils.getAdsDirectory(adId);
        Path targetFile = directory.resolve(filename);

        saveFile(directory, targetFile, file);

        return imagePathUtils.buildAdImageUrl(adId, filename);
    }

    @Override
    public String saveUserImage(Integer userId, MultipartFile file) {
        FileTypeUtils.validateImage(file);

        String extension = FileTypeUtils.resolveExtension(file);
        String filename = "avatar" + extension;

        Path directory = imagePathUtils.getUsersDirectory(userId);
        Path targetFile = directory.resolve(filename);

        saveFile(directory, targetFile, file);

        return imagePathUtils.buildUserImageUrl(userId, filename);
    }

    @Override
    public void deleteImageIfExists(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return;
        }

        try {
            Path physicalPath = imagePathUtils.resolvePhysicalPathFromImageUrl(imagePath);

            if (physicalPath != null && Files.exists(physicalPath)) {
                Files.delete(physicalPath);

                Path parent = physicalPath.getParent();
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
                if (e.getCause() instanceof IOException ioException) {
                    throw ioException;
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