package com.peg2s.controllers;

import com.peg2s.models.User;
import com.peg2s.models.enums.Role;
import com.peg2s.models.enums.Sex;
import com.peg2s.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Arrays;


@Controller
@SessionAttributes({"login", "sex"})
public class RegistrationController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public RegistrationController(@Lazy PasswordEncoder passwordEncoder,
                                  UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping("register")
    public String registration(Model model) {
        model.addAttribute("sex", Arrays.asList(Sex.values()));
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        User registeredUser = userRepository.findByLoginIgnoreCase(user.getLogin());
        if(!user.checkInputOk()) {
           model.addAttribute("warning", "Все поля формы обязательны к заполнению!");
            return "register";
        } else if (registeredUser != null) {
            model.addAttribute("warning", "Пользователь с таким логином существует!");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        userRepository.save(user);
        model.addAttribute("warning", "Вы успешно зарегистрированы. Ваш логин " + user.getLogin());
        return "register";
    }

}
