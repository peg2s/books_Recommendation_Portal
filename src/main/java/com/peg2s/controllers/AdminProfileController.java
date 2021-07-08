package com.peg2s.controllers;

import com.peg2s.models.User;
import com.peg2s.models.enums.Role;
import com.peg2s.models.enums.Sex;
import com.peg2s.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequestMapping("/admin/")
@SessionAttributes({"sex", "login"})
public class AdminProfileController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private List<User> userList;

    public AdminProfileController(UserRepository userRepository,
                                  @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String getUserProfile(Model model, HttpServletRequest request) {
        User user = userRepository.findByLoginIgnoreCase(request.getUserPrincipal().getName());
        userList = (List<User>) userRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("login", user.getLogin());
        model.addAttribute("sex", Arrays.asList(Sex.values()));
        model.addAttribute("userList", userList);
        return "adminProfile";
    }

    @PostMapping("/update")
    public String updateProfile(User user) {
        String pass = user.getPassword();
        User userFromDb = userRepository.findById(user.getId()).get();
        if(!pass.equals(userFromDb.getPassword())) {
            user.setPassword(passwordEncoder.encode(pass));
        }
        userRepository.save(user);
        return "adminProfile";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/updateByAdmin")
    public String updateProfileByAdmin(User user, String id, String isAdmin) {
        userList = (List<User>) userRepository.findAll();

        String pass = user.getPassword();
        User userFromDb = userRepository.findById(Long.valueOf(id)).get();
        if(!pass.equals(userFromDb.getPassword())) {
            userFromDb.setPassword(passwordEncoder.encode(pass));
        }
        if(isAdmin != null && !userFromDb.getRoles().contains(Role.ROLE_ADMIN)) {
            userFromDb.getRoles().add(Role.ROLE_ADMIN);
        } else if (isAdmin == null && userFromDb.getRoles().contains(Role.ROLE_ADMIN)) {
            userFromDb.getRoles().remove(Role.ROLE_ADMIN);
        }
        userFromDb.setDateOfBirth(user.getDateOfBirth());
        userFromDb.setLogin(user.getLogin());
        userRepository.save(userFromDb);
        return "redirect:/admin/";
    }
}
