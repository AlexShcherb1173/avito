package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.Announcemenst;

@RestController
@RequestMapping
public class AnnouncementsController {
    @GetMapping("/ads")
    public ResponseEntity<?> getAll() {
        return null;
    }

    @PostMapping("/ads")
    public ResponseEntity<?> get(@RequestBody Announcemenst announcemenst) {
        return null;
    }

    @GetMapping("/ads/{id}")
    public ResponseEntity<?> getInfo(@RequestBody int id) {
        return null;
    }

    @DeleteMapping("/ads/{id}")
    public ResponseEntity<?> delete(@RequestBody int id) {
        return null;
    }

    @PatchMapping("/ads/{}ad")
    public ResponseEntity<?> change(@RequestBody int id) {
        return null;
    }

    @GetMapping("/ads/me")
    public ResponseEntity<?> getinfo() {
        return null;
    }

    @PatchMapping("/ads/{id}/image")
    public ResponseEntity<?> getImage(@RequestBody int id) {
        return null;
    }

}
