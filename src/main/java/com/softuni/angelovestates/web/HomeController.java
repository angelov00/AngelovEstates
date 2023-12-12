package com.softuni.angelovestates.web;

import com.softuni.angelovestates.model.DTO.ReviewAddDTO;
import com.softuni.angelovestates.service.OfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final OfferService offerService;

    public HomeController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/")
    public String getHome(Model model) {

        long offers = this.offerService.getOffersCount();
        model.addAttribute("offersCount", offers);
        model.addAttribute("reviewAddDTO", new ReviewAddDTO());
        return "home";
    }

    @GetMapping("/about-us")
    public String getAboutUs() {
        return "about-us";
    }

    @GetMapping("/contacts")
    public String getContacts() {
        return "contacts";
    }

}
