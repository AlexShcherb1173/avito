package ru.avito.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImagePathUtils {

    private final Path rootDir;

    public ImagePathUtils(@Value("${app.images.dir:/images}") String rootDir) {
        this.rootDir = Paths.get(rootDir).toAbsolutePath().normalize();
    }

    public Path getRootDir() {
        return rootDir;
    }

    public Path getAdsDirectory(Integer adId) {
        return rootDir.resolve(Paths.get("ads", String.valueOf(adId))).normalize();
    }

    public Path getUsersDirectory(Integer userId) {
        return rootDir.resolve(Paths.get("users", String.valueOf(userId))).normalize();
    }

    public String buildAdImageUrl(Integer adId, String filename) {
        return "/images/ads/" + adId + "/" + filename;
    }

    public String buildUserImageUrl(Integer userId, String filename) {
        return "/images/users/" + userId + "/" + filename;
    }

    public Path resolvePhysicalPathFromImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }

        String normalizedUrl = imageUrl.trim();

        if (normalizedUrl.startsWith("/")) {
            normalizedUrl = normalizedUrl.substring(1);
        }

        if (normalizedUrl.startsWith("images/")) {
            normalizedUrl = normalizedUrl.substring("images/".length());
        }

        return rootDir.resolve(normalizedUrl).normalize();
    }
}