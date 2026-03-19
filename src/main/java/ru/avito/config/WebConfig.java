package ru.avito.config;

@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/images/**")
            .addResourceLocations("file:images/");
}
