package com.peg2s.controllers;

import com.peg2s.models.Book;
import com.peg2s.models.PersonalRating;
import com.peg2s.repositories.BookRepository;
import com.peg2s.repositories.GenreRepository;
import com.peg2s.repositories.RatingRepository;
import com.peg2s.repositories.UserRepository;
import com.peg2s.service.BookService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@SessionAttributes({"login", "books"})
public class BookController {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final BookService bookService;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final HttpServletRequest request;

    public BookController(BookRepository bookRepository, GenreRepository genreRepository,
                          BookService bookService, RatingRepository ratingRepository,
                          UserRepository userRepository, HttpServletRequest request) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.ratingRepository = ratingRepository;
        this.bookService = bookService;
        this.userRepository = userRepository;
        this.request = request;
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/addBook")
    public String getAddBookForm() {
        return "addBook";
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/addBook")
    public String addBook(Book book,
                          @RequestParam("genresList") String genresList,
                          @RequestParam("authorsNames") String authorsNames) {
        book = bookService.fillBookForSavingToDB(book, authorsNames, genresList);
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/randomBook")
    public String getRandomBook(Model model) {
        Book book = bookRepository.getRandomBook();
        if (request.getUserPrincipal() != null) {
            String login = request.getUserPrincipal().getName();
            Optional<PersonalRating> rating = ratingRepository.findByBook_IdAndUser_Login(book.getId(), login);
            rating.ifPresent(personalRating -> model.addAttribute("rating", personalRating));
            model.addAttribute("message", "Эй, " + login + "! Может стоит прочесть эту книгу?");
            model.addAttribute("login", login);
        }
        model.addAttribute("book", book);
        model.addAttribute("message", "Может стоит прочесть эту книгу?");

        return "oneBook";
    }

    @GetMapping("/book")
    public String getBookInfo(Model model, @RequestParam("id") String bookId) {
        Book book = bookRepository.findById(Long.valueOf(bookId)).get();
        model.addAttribute("message", "Книга: " + book.getTitle()
                + ", автор: " + book.getAuthorsAsString());
        if (request.getUserPrincipal() != null) {
            String login = request.getUserPrincipal().getName();
            model.addAttribute("login", login);
            Optional<PersonalRating> rating = ratingRepository.findByBook_IdAndUser_Login(book.getId(), login);
            rating.ifPresent(personalRating -> model.addAttribute("rating", personalRating));
        }
        model.addAttribute("book", book);
        return "oneBook";
    }

    @GetMapping({"/books", "/admin/books"})
    public String getBooks(Model model) {
        if (request.getUserPrincipal() != null) {
            String login = request.getUserPrincipal().getName();
            model.addAttribute("login", login);
            model.addAttribute("ratedBooks", ratingRepository.findByUser_login(login));
        }
        model.addAttribute("books", bookRepository.findByIsApprovedTrue());
        model.addAttribute("genres", genreRepository.findAll());
        return "books";
    }

    @PostMapping("/searchBooks")
    public String searchBooks(Model model, String searchText, String scope) {
        List<Book> searchResults = bookService.searchBooks(searchText, scope);
        if (searchResults.size() != 0) {
            model.addAttribute("books", searchResults);
        } else {
            model.addAttribute("message", "По данному запросу мы не нашли ни одной книги. ☹");
        }
        model.addAttribute("genres", genreRepository.findAll());
        return "books";
    }

    @PostMapping("/moderationSearch")
    public String moderationSearch(Model model, String searchText, String selectedScope) {
        List<Book> listForSearch = (List<Book>) model.getAttribute("books");
        if (listForSearch.size() != 0) {
            listForSearch = listForSearch.stream()
                    .filter(book -> book.findAnyMatch(searchText))
                    .collect(Collectors.toList());
            model.addAttribute("books", listForSearch);
        } else {
            model.addAttribute("message", "По данному запросу мы не нашли ни одной книги. ☹");
        }
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("fragment", "approveBooksFragment");
        model.addAttribute("selectedScope", selectedScope);

        return "adminProfile";
    }

    @PostMapping("/books")
    public String getFilteredBooks(Model model, @RequestParam("genre") String genre) {
        model.addAttribute("books", bookService.getFilteredBooksByGenre(genre));
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("selectedGenre", genre);
        return "books";
    }

    @GetMapping("/ratedBooks")
    public String getUserRatedBooks(Model model) {
        String login = request.getUserPrincipal().getName();
        bookService.addRatedBooks(login, model);
        return "books";
    }

    @GetMapping("/admin/ratedBooks")
    public String getRatedBooksForProfile(Model model) {
        String login = request.getUserPrincipal().getName();
        bookService.addRatedBooks(login, model);
        model.addAttribute("fragment", "ratedBooksFragment");
        model.addAttribute("user", userRepository.findByLoginIgnoreCase(login));
        return "adminProfile";
    }

    @PostMapping("/rateBook")
    @ResponseBody
    public void rateBook(Model model, HttpServletRequest request,
                         String selected_rating, String bookId) {
        String login = request.getUserPrincipal().getName();
        bookService.rateBook(login, bookId, selected_rating);
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
    }

    @GetMapping("/authorBooks")
    public ModelAndView getBooksByAuthor(@RequestParam String id,
                                         @RequestParam String author,
                                         Model model) {
        model.addAttribute("books", bookRepository.findAllByIsApprovedTrueAndAuthors_Id(Long.parseLong(id)));
        model.addAttribute("author", author);
        return new ModelAndView("authorBooks", "model", model);
    }

    @GetMapping("/genreBooks")
    public ModelAndView getBooksByGenre(@RequestParam String genre,
                                        Model model) {
        model.addAttribute("books", bookService.getFilteredBooksByGenre(genre));
        model.addAttribute("genre", genre);
        return new ModelAndView("genreBooks", "model", model);
    }

    @GetMapping("/recommendations")
    public String showPersonalRecommendations(Model model, HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            model.addAttribute("login", request.getUserPrincipal().getName());
        }
        return "underConstruction";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/booksModeration")
    public String getBooksForModeration(Model model) {
        if (!model.containsAttribute("selectedScope")) {
            model.addAttribute("selectedScope", "Только непроверенные");
        }
        model.addAttribute("books",
                bookService.getBooksForModeration((String) Objects.requireNonNull(
                        model.getAttribute("selectedScope"))));
        model.addAttribute("fragment", "approveBooksFragment");
        return "adminProfile";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/booksModeration")
    public String bookModeration(Model model, String selectedScope) {
        model.addAttribute("books", bookService.getBooksForModeration(selectedScope));
        model.addAttribute("selectedScope", selectedScope);
        model.addAttribute("fragment", "approveBooksFragment");
        return "adminProfile";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/removeBook")
    public String removeBook(Model model, @RequestParam String id) {
        bookService.removeBook(id);
        model.addAttribute("books",
                bookService.getBooksForModeration((String) Objects.requireNonNull(
                        model.getAttribute("selectedScope"))));
        model.addAttribute("fragment", "approveBooksFragment");
        return "adminProfile";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/editBook")
    public String editBook(Model model, Book book) {
        bookService.editBookByAdmin(book);
        model.addAttribute("books", null);
        model.addAttribute("fragment", "approveBooksFragment");
        return "redirect:/booksModeration";
    }
}
