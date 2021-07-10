package com.peg2s.repositories;

import com.peg2s.models.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findAllByGenres_Genre(@Param("genre") String genre);

    List<Book> findAllByAuthors_Id(Long id);

    @Query(value = "SELECT * FROM books ORDER BY RANDOM() LIMIT 18", nativeQuery = true)
    List<Book> getRandomBooks();

    @Query(value = "SELECT * FROM books ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Book getRandomBook();

    List<Book> findByIsApprovedFalse();

    List<Book> findByIsApprovedTrue();

    Optional<Book> findById(Long id);
}
