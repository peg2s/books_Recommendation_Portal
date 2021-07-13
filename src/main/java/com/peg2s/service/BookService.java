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
public class BookService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final AuthorRepository authorRepository;

    public BookService(GenreRepository genreRepository, BookRepository bookRepository,
                       UserRepository userRepository, RatingRepository ratingRepository, AuthorRepository authorRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> searchBooks(String text, String scope) {
        switch (scope) {
            case "По автору":
                return bookRepository.findAllByAuthors_NameContains(text);
            case "По названию":
                return bookRepository.findAllByTitleContains(text);
            default:
            return bookRepository.searchEverywhere(text, text, text, text);
        }
    }

    public void rateBook(String login, String bookId, String selected_rating) {
        Integer rating = Integer.parseInt(selected_rating);
        Long book_id = Long.valueOf(bookId);
        Long user_id = userRepository.findByLoginIgnoreCase(login).getId();
        if (ratingRepository.existsByBook_IdAndUser_Id(Long.valueOf(bookId), user_id)) {
            overrideExistingRating(book_id, user_id, rating);
        } else {
            rateNewBook(login, book_id, rating);
        }
    }

    private void rateNewBook(String login, Long bookId, Integer rate) {
        PersonalRating rating = new PersonalRating();
        Book book = bookRepository.findById(bookId).get();
        rating.setBook(book);
        rating.setUser(userRepository.findByLoginIgnoreCase(login));
        rating.setRate(rate);
        ratingRepository.save(rating);
    }

    private void overrideExistingRating(Long bookId, Long userId, Integer selected_rating) {
        PersonalRating rating = ratingRepository.findByBook_IdAndUser_Id(bookId, userId);
        rating.setRate(selected_rating);
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
        book.setIsApproved(false);
        return book;
    }

    public List<Book> getBooksForApprove() {
        return bookRepository.findByIsApprovedFalse();
    }

    public void approveBooks(String bookId, String isApprovedByAdmin) {
        Book book = bookRepository.findById(Long.valueOf(bookId)).get();
        book.setIsApproved(isApprovedByAdmin != null);
        bookRepository.save(book);
    }

    public void importBooksToDB() {

    }
}

