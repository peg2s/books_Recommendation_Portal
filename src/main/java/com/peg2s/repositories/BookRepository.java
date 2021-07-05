package com.peg2s.repositories;

import com.peg2s.models.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findAllByGenres_Genre(@Param("genre") String genre);
    List<Book> findAllByAuthors_Id(Long id);
}
