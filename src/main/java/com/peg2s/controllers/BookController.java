package com.peg2s.controllers;

import com.peg2s.models.Book;
import com.peg2s.repositories.BookRepository;
import com.peg2s.repositories.GenreRepository;
import com.peg2s.repositories.RatingRepository;
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
    private final HttpServletRequest request;

    public BookController(BookRepository bookRepository, GenreRepository genreRepository, BookProcessingHelper bookProcessingHelper, RatingRepository ratingRepository, HttpServletRequest request) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.bookProcessingHelper = bookProcessingHelper;
        this.ratingRepository = ratingRepository;
        this.request = request;
    }

    @GetMapping("/addBook")
    public String getAddBookForm() {
        return "addBook";
    }

    @PostMapping("/addBook")
    @ResponseBody
    public String addBook(Book book,
                          @RequestParam("genresList") String genresList,
                          @RequestParam("authorsNames") String authorsNames) {
        book = bookProcessingHelper.fillBookForSavingToDB(book, authorsNames, genresList);
        bookRepository.save(book);
        return "main";
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
    public Model getFilteredBooks(Model model, @RequestParam("genre") String genre) {
        model.addAttribute("books", bookProcessingHelper.getFilteredBooksByGenre(genre));
        model.addAttribute("genres", genreRepository.findAll());
        return model;
    }

    @PostMapping("/rateBook")
    @ResponseBody
    public void rateBook(Model model, HttpServletRequest request, String selected_rating, String book) {
        String login = request.getUserPrincipal().getName();
        bookProcessingHelper.rateBook(book, login, selected_rating);
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

}
