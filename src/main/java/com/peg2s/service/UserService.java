package com.peg2s.service;

import com.peg2s.models.User;
import com.peg2s.models.enums.Role;
import com.peg2s.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByLoginIgnoreCase(s);
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void updateProfile(User user) {
        String pass = user.getPassword();
        user = userRepository.findById(user.getId()).get();
        user = user.toBuilder()
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .login(user.getLogin())
                .password(passwordEncoder.encode(pass))
                .sex(user.getSex())
                .build();
        userRepository.save(user);
    }

    public void updateUserProfileByAdmin(User user, String id, String isAdmin) {
        String pass = user.getPassword();

        User userFromDb = userRepository.findById(Long.valueOf(id)).get();
        if (!pass.equals(userFromDb.getPassword())) {
            userFromDb.setPassword(passwordEncoder.encode(pass));
        }
        if (isAdmin != null && !userFromDb.getRoles().contains(Role.ROLE_ADMIN)) {
            userFromDb.getRoles().add(Role.ROLE_ADMIN);
        } else if (isAdmin == null) {
            userFromDb.getRoles().remove(Role.ROLE_ADMIN);
        }
        userFromDb.setDateOfBirth(user.getDateOfBirth());
        userFromDb.setLogin(user.getLogin());
        userRepository.save(userFromDb);
    }

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
