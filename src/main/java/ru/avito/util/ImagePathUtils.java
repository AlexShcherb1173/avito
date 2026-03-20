package ru.avito.util;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class ImagePathUtils {

    private static final String ROOT_DIR = "images";

    public Path getAdsDirectory(Integer adId) {
        return Paths.get(ROOT_DIR, "ads", String.valueOf(adId));
    }

    public Path getUsersDirectory(Integer userId) {
        return Paths.get(ROOT_DIR, "users", String.valueOf(userId));
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

        String normalized = imageUrl.startsWith("/") ? imageUrl.substring(1) : imageUrl;
        return Paths.get(normalized);
    }
}