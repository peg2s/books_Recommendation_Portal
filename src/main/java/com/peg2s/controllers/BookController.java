package com.peg2s.controllers;

import com.peg2s.models.Book;
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
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes("login")
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
        if (request.getUserPrincipal() != null) {
            String login = request.getUserPrincipal().getName();
            model.addAttribute("login", login);
        }
        model.addAttribute("book", bookRepository.getRandomBook());

        return "randomBook";
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

    @PostMapping("/books")
    public String getFilteredBooks(Model model, @RequestParam("genre") String genre) {
        model.addAttribute("books", bookService.getFilteredBooksByGenre(genre));
        model.addAttribute("genres", genreRepository.findAll());
        return "books";
    }

    @GetMapping("/ratedBooks")
    public String getUserRatedBooks(Model model) {
        String login = request.getUserPrincipal().getName();
        bookService.addRatedBooks(login,model);
        return "books";
    }

    @GetMapping("/admin/ratedBooks")
    public String getRatedBooksForProfile(Model model) {
        String login = request.getUserPrincipal().getName();
        bookService.addRatedBooks(login,model);
        model.addAttribute("fragment", "ratedBooksFragment");
        model.addAttribute("user", userRepository.findByLoginIgnoreCase(login));
        return "adminProfile";
    }

    @PostMapping("/rateBook")
    @ResponseBody
    public void rateBook(Model model, HttpServletRequest request,
                         String selected_rating, String bookId) {
        String login = request.getUserPrincipal().getName();
        bookService.rateBook(login, bookId,selected_rating);
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
    }

    @GetMapping("/authorBooks")
    @ResponseBody
    public ModelAndView getBooksByAuthor(@RequestParam String id,
                                         @RequestParam String author,
                                         Model model) {
        model.addAttribute("books", bookRepository.findAllByAuthors_Id(Long.parseLong(id)));
        model.addAttribute("author", author);
        return new ModelAndView("authorBooks", "model", model);
    }

    @GetMapping("/genreBooks")
    @ResponseBody
    public ModelAndView getBooksByGenre(@RequestParam String genre,
                                         Model model) {
        model.addAttribute("books", bookService.getFilteredBooksByGenre(genre));
        model.addAttribute("genre", genre);
        return new ModelAndView("genreBooks", "model", model);
    }

    @GetMapping("/recommendations")
    public String showPersonalRecommendations(Model model, HttpServletRequest request) {
        if(request.getUserPrincipal() != null) {
            model.addAttribute("login", request.getUserPrincipal().getName());
        }
        return "underConstruction";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/approveBooks")
    public String getBooksForApprove(Model model, HttpServletRequest request, HttpSession session) {
        model.addAttribute("books", bookService.getBooksForApprove());
        model.addAttribute("fragment", "approveBooksFragment");
        return "adminProfile";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/approveBooks")
    public String approveBooks(Model model, String id, String isApproved) {
        bookService.approveBooks(id, isApproved);
        model.addAttribute("books", bookService.getBooksForApprove());
        model.addAttribute("fragment", "approveBooksFragment");
        return "adminProfile";
    }
}
