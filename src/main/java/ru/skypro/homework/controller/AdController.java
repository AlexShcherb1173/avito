package ru.skypro.homework.controller;

import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;

import java.util.Collection;

@RestController
@RequestMapping("/ads")
public class AdController {

    private final AdService adService;
    private final CommentService commentService;

    public AdController(AdService adService, CommentService commentService) {
        this.adService = adService;
        this.commentService = commentService;
    }

    @GetMapping("/all")
    public Ads getAllAd(){
        return adService.getAllAd();
    }
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Ad> addAd(@RequestBody Ad ad,
                                   @RequestPart(value = "image", required = false) MultipartFile image){
        if(adService.addAd(ad, image)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/{id}/comments")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Collection<CommentEntity> getAllCommentsByAdId(@PathVariable Long id){
        return commentService.getComments(id);
    }
    @PostMapping("/{id}/comments")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public void addComment(@PathVariable Integer id, @RequestBody Comment comment){
        commentService.addComment(id, comment);

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAdById(@PathVariable Integer id){
        if(adService.getAdById(id) != null){
            return ResponseEntity.ok().body(adService.getAdById(id));
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAd(@PathVariable Integer id){
        if(adService.removeAd(id) != null){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateAd(@RequestBody CreateOrUpdateAd ad, @PathVariable Integer id){
        if(adService.updateAd(ad, id) != null){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId){
        if(commentService.removeComment(adId, commentId) != null){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/{id}/comment")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateComment(@PathVariable Integer id, @RequestBody CreateOrUpdateComment comment){
        if(commentService.updateComment(id, comment) != null){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/{id}/image")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public void updateImageAd(@PathVariable Integer id,
                              @RequestParam("image") MultipartFile image){
        adService.updateImageAd(id, image);


    }
}
