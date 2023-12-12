package com.softuni.angelovestates.web;

import com.softuni.angelovestates.service.OfferService;
import com.softuni.angelovestates.service.ReviewService;
import com.softuni.angelovestates.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final OfferService offerService;
    private final ReviewService reviewService;

    public AdminController(UserService userService, OfferService offerService, ReviewService reviewService) {
        this.userService = userService;
        this.offerService = offerService;
        this.reviewService = reviewService;
    }

    @GetMapping()
    public String getAdminPanel(Model model){

        long userCount = userService.getUsersCount();
        long agentCount = userService.getAgentsCount();
        long offerCount = offerService.getOffersCount();
        long offersThisWeek = offerService.getOffersThisWeekCount();
        double averageRating = reviewService.getAverageRate();

        model.addAttribute("userCount", userCount);
        model.addAttribute("agentCount", agentCount);
        model.addAttribute("offerCount", offerCount);
        model.addAttribute("offersThisWeek", offersThisWeek);
        model.addAttribute("averageRating", averageRating);

        return "admin-panel";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/promoteToAdmin")
    public String makeAdmin(@RequestParam String email, Model model) {

        if (this.userService.userByEmail(email).isEmpty()) {
            model.addAttribute("userNotFound", true);
        } else {
            model.addAttribute("adminPromotionSuccess", true);
            this.userService.promoteToAdmin(email);
        }
        return "admin-panel";
    }
}
