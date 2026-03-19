package ru.avito.repository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByAdId(Integer adId);
}
