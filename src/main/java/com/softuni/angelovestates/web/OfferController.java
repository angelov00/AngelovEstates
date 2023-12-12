package com.softuni.angelovestates.web;

import com.softuni.angelovestates.model.DAO.OfferDetailsDAO;
import com.softuni.angelovestates.model.DAO.OfferPreviewDAO;
import com.softuni.angelovestates.model.DTO.OfferAddDTO;
import com.softuni.angelovestates.model.DTO.OfferSearchDTO;
import com.softuni.angelovestates.exception.OfferNotFoundException;
import com.softuni.angelovestates.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/offers")
public class OfferController {
    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/add")
    public String getAddOffer() {
        return "add-offer";
    }

    @PostMapping("/add")
    public String postAddOffer(@Valid @ModelAttribute OfferAddDTO offerAddDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) throws IOException {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("offerAddDTO", offerAddDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.offerAddDTO",
                            bindingResult);
            return "redirect:/offers/add";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.offerService.addOffer(offerAddDTO, authentication.getName());
        return "redirect:/";
    }

    @GetMapping("/search")
    public String getSearch() {
        return "search-offer";
    }

    @PostMapping ("/search/result")
    public String getOffers(@Valid @ModelAttribute OfferSearchDTO offerSearchDTO, Model model,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            @RequestParam(defaultValue = "0") int pageNumber,
                            @RequestParam(defaultValue = "6") int pageSize) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("offerSearchDTO", offerSearchDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.offerSearchDTO", bindingResult);
            return "redirect:/offers/search";
        }

        //Pageable
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        Page<OfferPreviewDAO> offersPage = this.offerService.findOffers(offerSearchDTO, pageable);

        model.addAttribute("offers", offersPage.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", offersPage.getTotalPages());
        model.addAttribute("offerSearchDTO", offerSearchDTO);


        return "show-offers";
    }

    @GetMapping("/details/{id}")
    public String getOfferById(@PathVariable("id") long id, Model model) {

        OfferDetailsDAO details = this.offerService.getDetails(id);

        if(details == null) {
            throw new OfferNotFoundException(id);
        }

        model.addAttribute("offer", details);
        return "offer-details";
    }

    @GetMapping("makeActive/{id}")
    @PreAuthorize("@offerService.isOfferOwner(authentication, #id)")
    public String makeActive(@PathVariable long id) {
        offerService.makeActive(id);
        return "redirect:/user/offers";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("@offerService.isOfferOwner(authentication, #id)")
    public String deleteOffer(@PathVariable long id) {
        offerService.deleteOfferByID(id);
        return "redirect:/user/offers";
    }

    @ExceptionHandler(OfferNotFoundException.class)
    public String handleOfferNotFoundException(OfferNotFoundException ex, Model model) {
        model.addAttribute("error", "Offer with id " + ex.getOfferId() + " not found");
        return "error-page";
    }

    //Model Attributes
    @ModelAttribute(name = "offerAddDTO")
    public OfferAddDTO offerAddDTO() {
        return new OfferAddDTO();
    }

    @ModelAttribute(name = "offerSearchDTO")
    public OfferSearchDTO offerSearchDTO() {
        return new OfferSearchDTO();
    }


}
