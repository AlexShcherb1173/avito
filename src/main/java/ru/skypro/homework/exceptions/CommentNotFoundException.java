package ru.skypro.homework.exceptions;

public class CommentNotFoundException extends RuntimeException{
    private final Long commentId;
    public CommentNotFoundException(Long commentId) {
        super("Comment with ID " + commentId + " is not found.");
        this.commentId = commentId;
    }
}
