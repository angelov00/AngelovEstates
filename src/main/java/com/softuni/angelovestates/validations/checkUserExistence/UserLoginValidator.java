package com.softuni.angelovestates.validations.checkUserExistence;

import com.softuni.angelovestates.model.DTO.UserLoginDTO;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.repository.UserRepository;
import com.softuni.angelovestates.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UserLoginValidator implements ConstraintValidator<ValidateLoginForm, UserLoginDTO> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserLoginValidator( UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void initialize(ValidateLoginForm constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserLoginDTO userLoginDTO, ConstraintValidatorContext context) {
        Optional<User> user = this.userRepository.findUserByEmail(userLoginDTO.getEmail());

        if (user.isEmpty()) {
            return false;
        }

        return passwordEncoder.matches(userLoginDTO.getPassword(), user.get().getPassword());
    }
}