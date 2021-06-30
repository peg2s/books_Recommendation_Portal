package com.peg2s.controllers;

import com.peg2s.service.BashOrgService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes({"login", "message"})
public class MainPageController {

    private final BashOrgService bashOrgService;

    public MainPageController(BashOrgService bashOrgService) {
        this.bashOrgService = bashOrgService;
    }

    @GetMapping("/")
    public String hello(HttpServletRequest request, ModelAndView modelAndView) {
        request.setAttribute("quotes", bashOrgService.getNQuotes(10));
        if (request.getUserPrincipal() != null) {
            request.setAttribute("login", request.getUserPrincipal().getName());
            request.setAttribute("login", request.getUserPrincipal().getName());
        }
        return "main";
    }

}
