package com.peg2s.repositories;

import com.peg2s.models.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    boolean existsByGenre(String genre);
    Genre findByGenre(String genre);
}
