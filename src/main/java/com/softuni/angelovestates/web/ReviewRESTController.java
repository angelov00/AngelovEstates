package com.softuni.angelovestates.web;

import com.softuni.angelovestates.model.DAO.ReviewDAO;
import com.softuni.angelovestates.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewRESTController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewRESTController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/all")
    public List<ReviewDAO> getRandomReviews() {
        return reviewService.getRandomReviews();
    }
}
