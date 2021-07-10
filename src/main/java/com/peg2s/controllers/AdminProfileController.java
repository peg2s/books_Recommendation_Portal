package com.peg2s.controllers;

import com.peg2s.models.User;
import com.peg2s.models.enums.Sex;
import com.peg2s.repositories.UserRepository;
import com.peg2s.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/")
@SessionAttributes({"sex", "login"})
@Secured("ROLE_ADMIN")
public class AdminProfileController {
    private final UserRepository userRepository;
    private final UserService userService;
    private List<User> userList;

    public AdminProfileController(UserRepository userRepository,
                                  UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/")
    public String getAdminProfile(Model model, HttpServletRequest request) {
        User user = userRepository.findByLoginIgnoreCase(request.getUserPrincipal().getName());
        userList = (List<User>) userRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("login", user.getLogin());
        model.addAttribute("sex", Arrays.asList(Sex.values()));
        model.addAttribute("fragment", "profileFragment");
        return "adminProfile";
    }

    @GetMapping("/users")
    public String getUserList(Model model, HttpServletRequest request, HttpSession session) {
        String login = (String) session.getAttribute("userName");
        model.addAttribute("login", login);
        model.addAttribute("userList", userList);
        model.addAttribute("fragment", "userListFragment");
        return "adminProfile";
    }

    @PostMapping("/update")
    public String updateProfile(User user, Model model) {
        userService.updateProfile(user);
        model.addAttribute("fragment", "profileFragment");
        return "adminProfile";
    }

    @PostMapping("/updateByAdmin")
    public String updateProfileByAdmin(User user, String id, String isAdmin, Model model) {
        userList = (List<User>) userRepository.findAll();
        userService.updateUserProfileByAdmin(user, id, isAdmin);
        model.addAttribute("fragment", "userListFragment");
        model.addAttribute("userList", userList);
        return "adminProfile";
    }
}
