package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Slf4j
@CrossOrigin//(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final AdService adService;
    private final AdMapper adMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public ImageController(AdService adService, AdMapper adMapper, UserService userService, UserMapper userMapper) {
        this.adService = adService;
        this.adMapper = adMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping(value = "/images/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) throws IOException {
        return ResponseEntity.ok(adService.getImage(id));
    }

    @GetMapping(value = "/avatars/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<byte[]> getAvatar(@PathVariable Integer id) throws IOException {
        return ResponseEntity.ok(userService.getImage(id));
    }
}

/*
* @RestController
@RequestMapping("avatars")
public class AvatarAnimalController {
    private final AvatarAnimalService avatarAnimalService;
    private final AvatarAnimalMapper avatarAnimalMapper;

    public AvatarAnimalController(AvatarAnimalService avatarAnimalService, AvatarAnimalMapper avatarAnimalMapper) {
        this.avatarAnimalService = avatarAnimalService;
        this.avatarAnimalMapper = avatarAnimalMapper;
    }

    @GetMapping
    public List<AvatarAnimalDTO> getPaginatedAvatars(
            @RequestParam int pageNumber,
            @RequestParam int pageSize
    ) {
        return avatarAnimalService.getPaginatedAvatarAnimals(pageNumber,pageSize)
                .stream()
                .map(avatarAnimalMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        AvatarAnimal avatar = avatarAnimalService.findAvatarAnimal(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar-from-file")
    public void downloadAvatarAnimal(@PathVariable Long id, HttpServletResponse response) throws IOException {
        AvatarAnimal avatarAnimal = avatarAnimalService.findAvatarAnimal(id);

        Path path = Path.of(avatarAnimal.getFilePath());
        try(InputStream is = Files.newInputStream(path);
            OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatarAnimal.getMediaType());
            response.setContentLength((int) avatarAnimal.getFileSize());
            is.transferTo(os);
        }
    }*/
