package com.peg2s.controllers;

import com.peg2s.models.enums.Sex;
import com.peg2s.models.User;
import com.peg2s.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Controller
@RequestMapping("/profile")
@SessionAttributes({"sex", "login"})
public class UserProfileController {
    private final UserRepository userRepository;

    public UserProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String getUserProfile(Model model, HttpServletRequest request) {
        User user = userRepository.findByLogin(request.getUserPrincipal().getName());
        model.addAttribute("user", user);
        model.addAttribute("login", user.getLogin());
        model.addAttribute("sex", Arrays.asList(Sex.values()));
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(Model model, User user) {
        userRepository.save(user);
        return "profile";
    }
}
