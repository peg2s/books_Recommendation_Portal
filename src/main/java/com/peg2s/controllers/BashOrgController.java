package com.peg2s.controllers;

import com.peg2s.service.BashOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BashOrgController {
    private final BashOrgService bashOrgService;

    @Autowired
    public BashOrgController(BashOrgService bashOrgService) {
        this.bashOrgService = bashOrgService;
    }

    @GetMapping("/randomQuote")
    public String getRandomQuote(Model model, HttpServletRequest request) {
        model.addAttribute("login", request.getUserPrincipal().getName());
        model.addAttribute("quote", bashOrgService.getRandomQuote());
        return "/randomQuote";
    }
}
