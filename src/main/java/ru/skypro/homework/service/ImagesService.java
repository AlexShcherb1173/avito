package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.FileTypeEnum;

import java.io.IOException;

public interface ImagesService {
    void saveFileToDisk(MultipartFile file, String targetFileName) throws IOException;

    byte[] loadFileByName(String fileName) throws IOException;

    String makeTargetFileName(String originalFilename, long pk, FileTypeEnum fileType);
}
