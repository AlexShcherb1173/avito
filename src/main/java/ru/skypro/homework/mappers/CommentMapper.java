package ru.skypro.homework.mappers;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public CommentEntity addComment(AdEntity adEntity, Comment comment){

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(adEntity.getId());
        commentEntity.setImageUserCreateComment(adEntity.getImageFileName());
        commentEntity.setUserNameCreateComment(adEntity.getFirstNameUser());
        commentEntity.setAd(adEntity);
        commentEntity.setText(comment.getText());
        return commentEntity;
    }
    public Comments getAllComments(List<CommentEntity> commentEntities){
        Comments comments = new Comments();
        int total = 0;
        List<Comment> commentList = new ArrayList<>();
        for (int i = 0; i < commentEntities.size(); i++) {
            Comment comment = new Comment();
            comment.setAuthor(commentEntities.get(i).getAd().getId());
            comment.setAuthorImage(commentEntities.get(i).getImageUserCreateComment());
            comment.setAuthorFirstName(commentEntities.get(i).getUserNameCreateComment());
            comment.setCreatedAt(commentEntities.get(i).getIdUser());
            comment.setPk(commentEntities.get(i).getId());
            comment.setText(commentEntities.get(i).getText());
            total++;
            commentList.add(comment);
        }
        comments.setResults(commentList);
        comments.setCount(total);
        return comments;
    }
    public CommentEntity updateComment(CommentEntity commentEntity, CreateOrUpdateComment comment){
        commentEntity.setText(comment.getText());
        return commentEntity;

    }


}
