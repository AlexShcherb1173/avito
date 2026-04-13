package ru.avito.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ImagePathUtilsTest {

    @Test
    void getRootDirShouldReturnNormalizedAbsolutePath() {
        ImagePathUtils imagePathUtils = new ImagePathUtils("target/test-images");

        Path rootDir = imagePathUtils.getRootDir();

        assertNotNull(rootDir);
        assertTrue(rootDir.isAbsolute());
        assertEquals(Paths.get("target/test-images").toAbsolutePath().normalize(), rootDir);
    }

    @Test
    void getAdsDirectoryShouldReturnDirectoryInsideRoot() {
        ImagePathUtils imagePathUtils = new ImagePathUtils("target/test-images");

        Path result = imagePathUtils.getAdsDirectory(15);

        assertEquals(
                Paths.get("target/test-images").toAbsolutePath().normalize().resolve(Paths.get("ads", "15")).normalize(),
                result
        );
    }

    @Test
    void getUsersDirectoryShouldReturnDirectoryInsideRoot() {
        ImagePathUtils imagePathUtils = new ImagePathUtils("target/test-images");

        Path result = imagePathUtils.getUsersDirectory(7);

        assertEquals(
                Paths.get("target/test-images").toAbsolutePath().normalize().resolve(Paths.get("users", "7")).normalize(),
                result
        );
    }

    @Test
    void buildAdImageUrlShouldReturnExpectedUrl() {
        ImagePathUtils imagePathUtils = new ImagePathUtils("target/test-images");

        String result = imagePathUtils.buildAdImageUrl(11, "photo.jpg");

        assertEquals("/images/ads/11/photo.jpg", result);
    }

    @Test
    void buildUserImageUrlShouldReturnExpectedUrl() {
        ImagePathUtils imagePathUtils = new ImagePathUtils("target/test-images");

        String result = imagePathUtils.buildUserImageUrl(22, "avatar.png");

        assertEquals("/images/users/22/avatar.png", result);
    }

    @Test
    void resolvePhysicalPathFromImageUrlShouldReturnNullWhenImageUrlIsNull() {
        ImagePathUtils imagePathUtils = new ImagePathUtils("target/test-images");

        Path result = imagePathUtils.resolvePhysicalPathFromImageUrl(null);

        assertNull(result);
    }

    @Test
    void resolvePhysicalPathFromImageUrlShouldReturnNullWhenImageUrlIsBlank() {
        ImagePathUtils imagePathUtils = new ImagePathUtils("target/test-images");

        Path result = imagePathUtils.resolvePhysicalPathFromImageUrl("   ");

        assertNull(result);
    }

    @Test
    void resolvePhysicalPathFromImageUrlShouldResolvePathWhenUrlStartsWithSlashAndImagesPrefix() {
        ImagePathUtils imagePathUtils = new ImagePathUtils("target/test-images");

        Path result = imagePathUtils.resolvePhysicalPathFromImageUrl("/images/users/5/avatar.jpg");

        assertEquals(
                Paths.get("target/test-images").toAbsolutePath().normalize()
                        .resolve(Paths.get("users", "5", "avatar.jpg")).normalize(),
                result
        );
    }

    @Test
    void resolvePhysicalPathFromImageUrlShouldResolvePathWhenUrlHasNoLeadingSlash() {
        ImagePathUtils imagePathUtils = new ImagePathUtils("target/test-images");

        Path result = imagePathUtils.resolvePhysicalPathFromImageUrl("images/ads/8/photo.jpg");

        assertEquals(
                Paths.get("target/test-images").toAbsolutePath().normalize()
                        .resolve(Paths.get("ads", "8", "photo.jpg")).normalize(),
                result
        );
    }

    @Test
    void resolvePhysicalPathFromImageUrlShouldResolvePathWhenUrlHasNoImagesPrefix() {
        ImagePathUtils imagePathUtils = new ImagePathUtils("target/test-images");

        Path result = imagePathUtils.resolvePhysicalPathFromImageUrl("users/3/avatar.jpg");

        assertEquals(
                Paths.get("target/test-images").toAbsolutePath().normalize()
                        .resolve(Paths.get("users", "3", "avatar.jpg")).normalize(),
                result
        );
    }
}