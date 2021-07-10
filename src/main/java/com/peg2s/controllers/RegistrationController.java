package com.peg2s.controllers;

import com.peg2s.models.User;
import com.peg2s.models.enums.Sex;
import com.peg2s.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Arrays;


@Controller
@SessionAttributes({"login", "sex"})
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("register")
    public String registration(Model model) {
        model.addAttribute("sex", Arrays.asList(Sex.values()));
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        return userService.registerUser(user, model);
    }

}
