package com.peg2s.controllers;

import com.google.common.collect.Lists;
import com.peg2s.models.PersonalRating;
import com.peg2s.models.User;
import com.peg2s.models.enums.Sex;
import com.peg2s.repositories.RatingRepository;
import com.peg2s.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/profile")
@SessionAttributes({"sex", "login"})
public class UserProfileController {
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public UserProfileController(UserRepository userRepository, RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    @GetMapping("/")
    public String getUserProfile(Model model, HttpServletRequest request) {
        User user = userRepository.findByLogin(request.getUserPrincipal().getName());
        List<PersonalRating> likedBooks = ratingRepository.findByUser_login(user.getLogin());
        List<List<PersonalRating>> partitionedListOfLikedBooks = Lists.partition(likedBooks, 4);
        model.addAttribute("partitionedListOfLikedBooks", partitionedListOfLikedBooks);
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
