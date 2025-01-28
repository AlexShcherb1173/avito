package ru.skypro.homework.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;

import java.sql.Timestamp;
import java.util.*;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    CommentMapper commentMapper = new CommentMapper();

    public CommentService(CommentRepository commentRepository, AdRepository adRepository) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
    }

    public Collection<CommentEntity> getComments(Long adId) {
        Collection<CommentEntity> listComment = commentRepository.findByAd_Id(adId);
        if(!listComment.isEmpty()){
            return listComment;
        }
        throw new NullPointerException("No comment found for ad id " + adId);

    }
    public void addComment(Integer adId, Comment comment) {
        Optional<AdEntity> ad = adRepository.findById(adId);
        if(ad.isPresent()){
            CommentEntity commentEntity = commentMapper.addComment(ad.get(), comment);
            commentRepository.save(commentEntity);
        }
        throw new NullPointerException("No comment found for ad id " + adId);

    }
    public CommentEntity removeComment(Integer adId, Integer commentId){
        Optional<AdEntity> ad = adRepository.findById(adId);
        Optional<CommentEntity> comment = commentRepository.findById(commentId);
        if(ad.isPresent() && comment.isPresent() && comment.get().getAd().getId().equals(ad.get().getId())){
            commentRepository.delete(comment.get());
            return comment.get();
        }

        throw new NullPointerException("No comment found for id" + commentId +" or for ad id " + adId);
    }

    public CreateOrUpdateComment updateComment(Integer commentId, CreateOrUpdateComment commentUpdate){
        Optional<CommentEntity> commentEntity = commentRepository.findById(commentId);
        if(commentEntity.isPresent()){

            CommentEntity commentEntityNew = commentMapper.updateComment(commentEntity.get(), commentUpdate);
            commentRepository.save(commentEntityNew);
            return commentUpdate;
        }
        throw new NoSuchElementException();
    }
}
