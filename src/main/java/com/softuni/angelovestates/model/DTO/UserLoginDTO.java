package com.softuni.angelovestates.model.DTO;

import com.softuni.angelovestates.validations.checkUserExistence.ValidateLoginForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class UserLoginDTO {

    @Email
    @NotBlank
    private String email;

    @Size(min = 5)
    @NotBlank
    private String password;


}
