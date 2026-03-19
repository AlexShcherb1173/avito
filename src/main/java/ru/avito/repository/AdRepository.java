package ru.avito.repository;

public interface AdRepository extends JpaRepository<Ad, Integer> {
    List<Ad> findAllByAuthorId(Integer authorId);
}
