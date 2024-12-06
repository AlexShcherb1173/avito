package ru.skypro.homework.service;



import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.entity.AvatarEntity;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;

public interface AvatarService {
//    AvatarEntity updateImage(Authentication authentication, MultipartFile file) throws IOException;
void updateImage(MultipartFile animalPhoto) throws IOException;
    ResponseEntity<byte[]> getAvatar() throws IOException;


    default String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}