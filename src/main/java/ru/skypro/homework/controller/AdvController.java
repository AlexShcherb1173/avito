package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdvertisementDTO;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.model.Advertisement;

import java.util.List;

@RestController
@RequestMapping("/ads")
public class AdvController {

    @GetMapping
    public List<Advertisement> getAllAdvertisements() {

        return null;
    }

    @PostMapping
    public ResponseEntity<String> addAdvertisements() {

        return ResponseEntity.ok("Реклама успешно добавлена");
    }

    @GetMapping("/{id}")
    public Advertisement getAdvertisementById(@PathVariable Long id) {

        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdvertisementById(@PathVariable Long id) {

        return ResponseEntity.ok("Реклама успешно удалена");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> deleteAdvertisementById(@RequestBody AdvertisementDTO advertisementDTO) {

        return ResponseEntity.ok("Реклама успешно обновлена");
    }

    @GetMapping("/me")
    public List<Advertisement> getAllAdvertisementsOfAuthUser() {

        return null;
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<String> updateAdvertisementImage(@PathVariable Long id) {

        return ResponseEntity.ok("Аватарка рекламы успешно обновлена");
    }

    @GetMapping("/{id}/comments")
    public List<CommentDTO> getCommentsOfAdvertisement(@PathVariable Long id) {

        return null;
    }

    @PostMapping("/{id}/comments")
    public CommentDTO addCommentToAdvertisement(@PathVariable Long id) {

        return null;
    }

    @DeleteMapping("/{addId}/comments/{commentId}")
    public CommentDTO deleteComment(@PathVariable Long addId, @PathVariable Long commentId) {

        return null;
    }

    @PatchMapping("/{addId}/comments/{commentId}")
    public CommentDTO updateComment(@PathVariable Long addId, @PathVariable Long commentId) {

        return null;
    }

}
