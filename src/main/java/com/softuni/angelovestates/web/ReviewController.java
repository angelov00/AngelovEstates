package com.softuni.angelovestates.web;

import com.softuni.angelovestates.model.DTO.ReviewAddDTO;
import com.softuni.angelovestates.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    private String addReview(@Valid @ModelAttribute(name = "reviewAddDTO") ReviewAddDTO reviewAddDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) throws IOException {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("reviewAddDTO", reviewAddDTO)
                    .addFlashAttribute("org.spring.framework.validation.BindingResult.reviewAddDTO",
                            bindingResult);
            return "redirect:/";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.reviewService.addReview(reviewAddDTO, authentication.getName());
        return "redirect:/";
    }

    @ModelAttribute(name = "reviewAddDTO")
    public ReviewAddDTO reviewAddDTO(@ModelAttribute(name = "reviewAddDTO") ReviewAddDTO reviewAddDTO) {
        return new ReviewAddDTO();
    }
}
