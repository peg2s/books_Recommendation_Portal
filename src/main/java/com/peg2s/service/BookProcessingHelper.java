package com.peg2s.service;

import com.peg2s.models.Author;
import com.peg2s.models.Book;
import com.peg2s.models.Genre;
import com.peg2s.models.PersonalRating;
import com.peg2s.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookProcessingHelper {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final AuthorRepository authorRepository;

    public BookProcessingHelper(GenreRepository genreRepository, BookRepository bookRepository,
                                UserRepository userRepository, RatingRepository ratingRepository, AuthorRepository authorRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.authorRepository = authorRepository;
    }

    public void rateBook(String bookId, String userLogin, String rate) {
        PersonalRating rating = new PersonalRating();
        Book book = bookRepository.findById(Long.parseLong(bookId)).get();
        rating.setBook(book);
        rating.setUser(userRepository.findByLoginIgnoreCase(userLogin));
        rating.setRate(Integer.parseInt(rate));
        ratingRepository.save(rating);
    }

    public void addRatedBooks(String login, Model model) {
        List<PersonalRating> ratings = ratingRepository.findByUser_login(login);
        List<Book> books = ratings.stream().map(PersonalRating::getBook).collect(Collectors.toList());
        model.addAttribute("login", login);
        model.addAttribute("ratedBooks", ratings);
        model.addAttribute("books", books);
        model.addAttribute("genres", genreRepository.findAll());
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
                .map(authorName -> {
                            authorName = authorName.trim();
                            if (authorRepository.existsByName(authorName)) {
                                return authorRepository.findByName(authorName);
                            } else {
                                return new Author(authorName);
                            }
                        }
                )
                .collect(Collectors.toList());
        ArrayList<Genre> genres = (ArrayList<Genre>) genresArray.stream()
                .map(genre -> {
                            genre = genre.trim();
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

