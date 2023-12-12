package com.softuni.angelovestates.service;

import com.softuni.angelovestates.model.DAO.ReviewDAO;
import com.softuni.angelovestates.model.DTO.ReviewAddDTO;
import com.softuni.angelovestates.model.entity.Review;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.repository.ReviewRepository;
import com.softuni.angelovestates.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public Review addReview(ReviewAddDTO reviewAddDTO, String email) {
        Optional<User> user = this.userRepository.findUserByEmail(email);
        Review review = modelMapper.map(reviewAddDTO, Review.class).setAuthor(user.get());
        this.reviewRepository.save(review);
        return review;
    }

    public List<ReviewDAO> getRandomReviews() {
        List<Review> reviews = this.reviewRepository.findAll();

        //Get random 3 reviews using collections.shuffle
        if (reviews.size() >= 3) {
            Collections.shuffle(reviews);
            reviews = reviews.subList(0, 3);
        }

        return reviews.stream().map(review -> new ReviewDAO()
                .setAuthor(review.getAuthor().getFirstName() + " " + review.getAuthor().getLastName())
                .setRating(review.getRating())
                .setComment(review.getComment())
        ).collect(Collectors.toList());
    }

    public double getAverageRate() {

        double average = this.reviewRepository.findAll()
                .stream()
                .mapToDouble(Review::getRating)
                .sum();

        long count = this.reviewRepository.count();
        return count == 0 ? 0 : average / this.reviewRepository.count();
    }
}
