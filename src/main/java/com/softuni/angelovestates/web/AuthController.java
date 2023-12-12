package com.softuni.angelovestates.web;

import com.softuni.angelovestates.model.DTO.UserLoginDTO;
import com.softuni.angelovestates.model.DTO.UserRegisterDTO;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.service.AuthService;
import com.softuni.angelovestates.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin(@RequestParam(value = "error", required = false)Boolean error, Model model, HttpSession session) {

        if (error != null && error) {
            model.addAttribute("error", true);
            // Clear the session attribute
            session.removeAttribute("error");
        }
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@Valid @ModelAttribute(name = "userLoginModel") UserLoginDTO userLoginDTO,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userLoginDTO", userLoginDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userLoginDTO",
                            bindingResult);
            return "redirect:login";
        }

        return "redirect:/";
    }

    @GetMapping("/register")
    public String getRegister(){
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute UserRegisterDTO userRegisterDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) throws IOException {

        Optional<User> doesExist = this.userService.userByEmail(userRegisterDTO.getEmail());

        if (bindingResult.hasErrors() || doesExist.isPresent()) {
            redirectAttributes.addFlashAttribute("userRegisterDTO", userRegisterDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterDTO",
                            bindingResult);
            if(doesExist.isPresent()) {
                redirectAttributes.addFlashAttribute("invalid", true);
            }
            return "redirect:register";
        }

        this.authService.register(userRegisterDTO);
        return "redirect:login";
    }


    //Model Attributes
    @ModelAttribute(name = "userLoginDTO")
    public UserLoginDTO userLoginDTO() {
        return new UserLoginDTO();
    }

    @ModelAttribute("userRegisterDTO")
    public UserRegisterDTO userRegisterDTO() {
        return new UserRegisterDTO();
    }


}
