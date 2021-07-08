package com.peg2s.models;

import com.peg2s.models.enums.Role;
import com.peg2s.models.enums.Sex;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Users")
@SuperBuilder(toBuilder = true)
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    private String login;

    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "about")
    private String aboutInfo;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    private String email;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    public boolean checkInputOk() {
      return StringUtils.isNotBlank(login)
              && StringUtils.isNotBlank(password)
              && StringUtils.isNotBlank(firstName)
              && StringUtils.isNotBlank(lastName)
              && StringUtils.isNotBlank(email)
              && sex != null;
    }

    public String getFullName() {
        return lastName + " " + firstName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN);
    }
}
