package ru.avito.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.avito.util.ImagePathUtils;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ImagePathUtils imagePathUtils;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourceLocation = "file:" + imagePathUtils.getRootDir().toString() + "/";

        registry.addResourceHandler("/images/**")
                .addResourceLocations(resourceLocation);
    }
}