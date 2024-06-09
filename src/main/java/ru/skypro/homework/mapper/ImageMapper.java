package ru.skypro.homework.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.FileProcessingException;
import ru.skypro.homework.model.Image;

import java.io.IOException;

@Mapper(componentModel = "spring")
public abstract class ImageMapper {

    @Mapping(target = "data", expression = "java(mapMultipartFileToByteArray(file))")
    @Mapping(target = "fileSize", expression = "java(getFileSize(file))")
    @Mapping(target = "mediaType", expression = "java(getMediaType(file))")
    @Mapping(target = "id", ignore = true)
    public abstract Image toImage(MultipartFile file);

    protected byte[] mapMultipartFileToByteArray(MultipartFile file) {
        try {
            return file != null ? file.getBytes() : null;
        } catch (IOException e) {
            throw new FileProcessingException("Ошибка преобразования MultipartFile в массив байтов", e);
        }
    }

    protected Long getFileSize(MultipartFile file) {
        return file != null ? file.getSize() : null;
    }

    protected String getMediaType(MultipartFile file) {
        return file != null ? file.getContentType() : null;
    }
}
