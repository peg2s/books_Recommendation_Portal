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

    List<Book> findAllByTitleContains(String title);

    List<Book> findAllByAuthors_NameContains(String name);

    @Query(value = "select * from books b " +
            "JOIN author_books ab on b.id = ab.book_id " +
            "JOIN authors a on author_id = a.id " +
            "where upper(b.title) like concat('%', upper(?1), '%') " +
            "or upper(b.description) like concat('%', upper(?2), '%') " +
            "or upper(b.annotation) like concat('%', upper(?3), '%')" +
            "or upper(a.name) like concat('%', upper(?4), '%')", nativeQuery = true)
    List<Book> searchEverywhere(String param1, String param2, String param3, String param4);

//    List<Book> findAllByTitleLikeOrDescriptionLikeOrAnnotationLike(String text);
}
