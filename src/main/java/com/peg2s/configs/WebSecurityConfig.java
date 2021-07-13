package com.peg2s.configs;

import com.peg2s.models.User;
import com.peg2s.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private PasswordEncoder passwordEncoder;

    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/", "/register", "/images/**", "/randomBook", "/books",
                        "fragments/**", "/authorBooks", "/genreBooks", "/searchBooks/**", "/book/**"
                )
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/loginError")
                .successHandler((request, response, authentication) -> {
                    request.getSession().setAttribute("userName",
                            ((User) authentication.getPrincipal()).getLogin());
                                        response.sendRedirect("/");
                            })
                            .permitAll()
                            .and()
                            .logout()
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                            .logoutSuccessUrl("/")
                            .permitAll();
                }

        @Override
        protected void configure (AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userService)
                    .passwordEncoder(passwordEncoder);
        }
    }