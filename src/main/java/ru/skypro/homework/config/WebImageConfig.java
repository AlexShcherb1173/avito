package ru.skypro.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebImageConfig implements WebMvcConfigurer {
    @Value("${images.root}")
    private String imagesRoot;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/users/**")
                .addResourceLocations("file:" + imagesRoot + "/users/");

        registry.addResourceHandler("/ads/**")
                .addResourceLocations("file:" + imagesRoot + "/ads/");
    }
}

