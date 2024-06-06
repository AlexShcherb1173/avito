package ru.skypro.homework.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

public class WorkWithFilesUtils {

    public static ImageEntity loadImage(MultipartFile file, String pathDir) throws IOException {
        String fileName = UUID.randomUUID() + "." + getExtension(file.getOriginalFilename());
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setNameImage(fileName);
        Path filePath = Path.of(pathDir, fileName);
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)){
            bis.transferTo(bos);
        }
        return imageEntity;
    }

    private static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static void deleteFile(String url) throws IOException {
        Path path = Path.of(url);
        Files.deleteIfExists(path);
    }

    public static byte[] getImage(String fileName, String pathDir) {
        Path filePath = Path.of(pathDir + "\\" + fileName);
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            bis.transferTo(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
}
