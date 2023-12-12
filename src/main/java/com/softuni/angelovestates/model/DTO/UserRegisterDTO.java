package com.softuni.angelovestates.model.DTO;

import com.softuni.angelovestates.validations.passwordMatcher.PasswordMatch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@PasswordMatch(password = "password", confirmPassword = "confirmPassword")
public class UserRegisterDTO {

    @Email
    private String email;

    @Size(min = 3)
    private String firstName;

    @Size(min = 3)
    private String lastName;

    @Size(min = 7)
    private String phoneNumber;

    @Size(min = 5)
    private String password;

    @Size(min = 5)
    private String confirmPassword;

    private boolean isAgent;

    //agent only fields
    private String companyName;

    private MultipartFile multipartFile;


}
