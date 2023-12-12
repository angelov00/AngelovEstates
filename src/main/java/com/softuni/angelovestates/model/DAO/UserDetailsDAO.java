package com.softuni.angelovestates.model.DAO;

import com.softuni.angelovestates.model.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class UserDetailsDAO {

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Set<Role> roles;
    private long offersCount;
    private String photoURL;
    private String company;

}
