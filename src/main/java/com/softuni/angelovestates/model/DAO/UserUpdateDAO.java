package com.softuni.angelovestates.model.DAO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class UserUpdateDAO {

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
