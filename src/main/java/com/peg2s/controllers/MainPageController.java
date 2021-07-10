package com.peg2s.controllers;

import com.peg2s.repositories.BookRepository;
import com.peg2s.repositories.GenreRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes({"login", "message"})
public class MainPageController {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    public MainPageController(BookRepository bookRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
    }

    @GetMapping("/")
    public String hello(HttpServletRequest request) {
        request.setAttribute("books", bookRepository.getRandomBooks());
        if (request.getUserPrincipal() != null) {
            request.setAttribute("login", request.getUserPrincipal().getName());
        }
        request.setAttribute("genres", genreRepository.findAll());

        return "main";
    }

}
