package ru.skypro.homework.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skypro.homework.mapper.AppMapper;

@Configuration
public class MapperConfiguration {
    @Bean
    public AppMapper appMapper() {
        return Mappers.getMapper(AppMapper.class);
    }
}