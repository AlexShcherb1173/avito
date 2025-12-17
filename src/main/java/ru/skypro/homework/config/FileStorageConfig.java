package ru.skypro.homework.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class FileStorageConfig {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.avatar.max-size:2097152}")
    private long avatarMaxSize;

    @Value("${app.avatar.allowed-types:image/jpeg,image/png,image/gif}")
    private String[] avatarAllowedTypes;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;
}
