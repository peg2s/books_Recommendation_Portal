package com.peg2s.controllers;

import com.peg2s.models.Book;
import com.peg2s.repositories.BookRepository;
import com.peg2s.repositories.GenreRepository;
import com.peg2s.service.BookProcessingHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BookController {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final BookProcessingHelper bookProcessingHelper;

    public BookController(BookRepository bookRepository, GenreRepository genreRepository, BookProcessingHelper bookProcessingHelper) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.bookProcessingHelper = bookProcessingHelper;
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
}
