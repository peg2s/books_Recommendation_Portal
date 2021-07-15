package com.peg2s.controllers;

import com.peg2s.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginAndLogoutController {

    private final UserService userService;

    public LoginAndLogoutController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @GetMapping("/loginError")
    public String showLoginError(Model model) {
       model.addAttribute("loginNotification", "Неправильный логин или пароль. " +
               "<br> " +
               "Эта ошибка также может возникать, если вы не активировали свою учетную запись по ссылке, отправленной на ваш почтовый ящик.");
        return "login";
    }

    @GetMapping("/common/getPasswordResetLink")
    public String getPasswordResetPage() {
        return "resetPassword";
    }

    @PostMapping("/common/getPasswordResetLink")
    public String getPasswordResetLink(Model model, String login) {
        return userService.getPasswordResetLink(model, login);
    }

    @GetMapping("/common/resetPassword")
    public String getResetPasswordPage(@RequestParam("id") String userLogin,
                                @RequestParam("code") String activationCode,
                                Model model) {
        return userService.resetPassword(userLogin, activationCode, model);
    }
}
