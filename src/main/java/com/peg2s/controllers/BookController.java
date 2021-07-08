package com.peg2s.controllers;

import com.peg2s.models.Book;
import com.peg2s.models.PersonalRating;
import com.peg2s.repositories.BookRepository;
import com.peg2s.repositories.GenreRepository;
import com.peg2s.repositories.RatingRepository;
import com.peg2s.repositories.UserRepository;
import com.peg2s.service.BookProcessingHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("login")
public class BookController {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final BookProcessingHelper bookProcessingHelper;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final HttpServletRequest request;

    public BookController(BookRepository bookRepository, GenreRepository genreRepository,
                          BookProcessingHelper bookProcessingHelper, RatingRepository ratingRepository,
                          UserRepository userRepository, HttpServletRequest request) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.ratingRepository = ratingRepository;
        this.bookProcessingHelper = bookProcessingHelper;
        this.userRepository = userRepository;
        this.request = request;
    }

    @GetMapping("/addBook")
    public String getAddBookForm() {
        return "addBook";
    }

    @PostMapping("/addBook")
    public String addBook(Book book,
                          @RequestParam("genresList") String genresList,
                          @RequestParam("authorsNames") String authorsNames) {
        book = bookProcessingHelper.fillBookForSavingToDB(book, authorsNames, genresList);
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String getBooks(Model model) {
        if (request.getUserPrincipal() != null) {
            String login = request.getUserPrincipal().getName();
            model.addAttribute("login", login);
            model.addAttribute("ratedBooks", ratingRepository.findByUser_login(login));
        }
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "books";
    }

    @PostMapping("/books")
    @ResponseBody
    public String getFilteredBooks(Model model, @RequestParam("genre") String genre) {
        model.addAttribute("books", bookProcessingHelper.getFilteredBooksByGenre(genre));
        model.addAttribute("genres", genreRepository.findAll());
        return "books";
    }

    @GetMapping("/ratedBooks")
    public String getUserRatedBooks(Model model) {
        String login = request.getUserPrincipal().getName();
        bookProcessingHelper.addRatedBooks(login,model);
        return "books";
    }

    @PostMapping("/rateBook")
    @ResponseBody
    public void rateBook(Model model, HttpServletRequest request,
                         String selected_rating, String bookId) {
        String login = request.getUserPrincipal().getName();
        Long userId = userRepository.findByLoginIgnoreCase(login).getId();
        if (ratingRepository.existsByBook_IdAndUser_Id(Long.valueOf(bookId), userId)) {
            PersonalRating rating = ratingRepository.findByBook_IdAndUser_Id(Long.valueOf(bookId), userId);
            rating.setRate(Integer.parseInt(selected_rating));
            ratingRepository.save(rating);
        } else {
            bookProcessingHelper.rateBook(bookId, login, selected_rating);
        }
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

    @GetMapping("/recommendations")
    public String showPersonalRecommendations(Model model, HttpServletRequest request) {
        if(request.getUserPrincipal() != null) {
            model.addAttribute("login", request.getUserPrincipal().getName());
        }
        return "underConstruction";
    }

}
