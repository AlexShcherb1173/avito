package ru.avito.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;
import ru.avito.exception.BadRequestException;

import java.util.List;

@UtilityClass
public class FileTypeUtils {

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/jpg",
            "image/gif",
            "image/webp"
    );

    public void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Image file is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException("Unsupported image type");
        }
    }

    public String resolveExtension(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null) {
            return ".jpg";
        }

        return switch (contentType.toLowerCase()) {
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            case "image/jpg", "image/jpeg" -> ".jpg";
            default -> ".jpg";
        };
    }
}