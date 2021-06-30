package com.peg2s.service;

import com.peg2s.models.Author;
import com.peg2s.models.Book;
import com.peg2s.models.Genre;
import com.peg2s.repositories.BookRepository;
import com.peg2s.repositories.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookProcessingHelper {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public BookProcessingHelper(GenreRepository genreRepository, BookRepository bookRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    public List<Book> getFilteredBooksByGenre(String genre) {
        if (genre.equalsIgnoreCase("all")) {
            return (List<Book>) bookRepository.findAll();
        } else {
            return bookRepository.findAllByGenres_Genre(genre);
        }
    }

    public Book fillBookForSavingToDB(Book book, String authorsNames, String genresList) {
        List<String> authorsNamesArray = Arrays.asList(authorsNames.split(","));
        List<String> genresArray = Arrays.asList(genresList.split(","));

        ArrayList<Author> authors = (ArrayList<Author>) authorsNamesArray.stream()
                .map(Author::new)
                .collect(Collectors.toList());

        ArrayList<Genre> genres = (ArrayList<Genre>) genresArray.stream()
                .map(genre -> {
                            if (genreRepository.existsByGenre(genre)) {
                                return genreRepository.findByGenre(genre);
                            } else {
                                return new Genre(genre);
                            }
                        }
                )
                .collect(Collectors.toList());

        genres.forEach(genre -> {
            genre.getBooks().add(book);
        });

        book.setGenres(genres);
        authors.forEach(author -> {
            book.getAuthors().add(author);
            author.getBooks().add(book);
        });

        book.setAuthors(authors);
        return book;
    }
}

