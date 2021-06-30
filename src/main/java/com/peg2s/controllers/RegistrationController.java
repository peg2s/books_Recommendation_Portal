package com.peg2s.controllers;

import com.peg2s.models.User;
import com.peg2s.models.enums.Role;
import com.peg2s.models.enums.Sex;
import com.peg2s.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Arrays;


@Controller
@SessionAttributes({"login", "sex"})
public class RegistrationController {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String registration(Model model) {
        model.addAttribute("sex", Arrays.asList(Sex.values()));
        return "/register";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        User registeredUser = userRepository.findByLogin(user.getLogin());
        if(!user.checkInputOk()) {
           model.addAttribute("warning", "Все поля формы обязательны к заполнению!");
            return "/register";
        } else if (registeredUser != null) {
            model.addAttribute("warning", "Пользователь с таким логином существует!");
            return "/register";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        model.addAttribute("warning", "Вы успешно зарегистрированы. Ваш логин " + user.getLogin());
        return "/register";
    }

}
