package com.peg2s.models;

import com.peg2s.models.enums.Role;
import com.peg2s.models.enums.Sex;
import org.apache.commons.lang3.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Users")
public class User {

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

    @Enumerated(EnumType.STRING)
    private Role role;

    public boolean checkInputOk() {
      return StringUtils.isNotBlank(login)
              && StringUtils.isNotBlank(password)
              && StringUtils.isNotBlank(firstName)
              && StringUtils.isNotBlank(lastName)
              && StringUtils.isNotBlank(email)
              && sex != null;
    }
}
