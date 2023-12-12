package com.softuni.angelovestates.web;

import com.softuni.angelovestates.model.DAO.OfferPreviewDAO;
import com.softuni.angelovestates.model.DAO.UserDetailsDAO;
import com.softuni.angelovestates.model.DAO.UserUpdateDAO;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.service.OfferService;
import com.softuni.angelovestates.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final OfferService offerService;

    @Autowired
    public UserController(UserService userService, OfferService offerService) {
        this.userService = userService;
        this.offerService = offerService;
    }

    @GetMapping("/offers")
    public String getMyOffers(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<OfferPreviewDAO> activeOffers = this.offerService.getUserActiveOffers(userDetails.getUsername());
        List<OfferPreviewDAO> expiredOffers = this.offerService.getUserExpiredOffers(userDetails.getUsername());
        model.addAttribute("offers", activeOffers);
        model.addAttribute("expiredOffers", expiredOffers);
        return "user-offers";
    }


    @GetMapping("/profile")
    public String getUserProfile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetailsDAO userDAO = this.userService.getUserDetails(authentication.getName());
        model.addAttribute("user", userDAO);
        return "user-profile";
    }

    @GetMapping("/edit")
    public String editProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        Optional<User> currentUser = this.userService.userByEmail(userDetails.getUsername());
        if (currentUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        model.addAttribute("user", currentUser.get());
        return "edit-profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute(name = "user") UserUpdateDAO updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> currentUser = this.userService.userByEmail(authentication.getName());

        if (currentUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        userService.updateUser(updatedUser, currentUser.get());
        return "redirect:/user/profile";
    }


    //Model Attributes
    @ModelAttribute(name = "user")
    public UserUpdateDAO userUpdateDAO() {
        return new UserUpdateDAO();
    }

}
