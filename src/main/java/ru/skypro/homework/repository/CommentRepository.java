package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skypro.homework.entity.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Integer> {

    @Query(value = "SELECT * FROM comments",
            nativeQuery = true)
    List<Comment> findAllComments();

    @Query(value = "SELECT * FROM comments " +
            "WHERE author_id= :author", nativeQuery = true)
    List<Comment> findAllCommentByAuthor(Integer author);

    @Query(value = "SELECT * FROM comment WHERE comment_id= :pk", nativeQuery = true)
    List<Comment> findCommentBycommentId(Integer pk);

}
