package com.peg2s.controllers;

import com.peg2s.models.User;
import com.peg2s.models.enums.Sex;
import com.peg2s.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/common/activate")
    public String activateUser(@RequestParam("id") String userLogin,
                               @RequestParam("code") String activationCode,
                               Model model) {
        if (userService.activateUser(userLogin, activationCode)) {
            model.addAttribute("warning", "Отлично! Теперь вы полноправный пользователь портала.\n" +
                    "Авторизуйтесь на сайте, используя форму входа в верхнем правом углу.");
        } else {
            model.addAttribute("warning", "Что-то пошло не так, попробуйте перейти по ссылке еще раз.\n" +
                    "Если проблема сохраняется, свяжитесь с администратором портала через telegram: @peg2sus");
        }
        return "register";
    }

}
