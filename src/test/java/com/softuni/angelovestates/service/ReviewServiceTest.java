package com.softuni.angelovestates.service;

import com.softuni.angelovestates.model.DAO.ReviewDAO;
import com.softuni.angelovestates.model.DTO.ReviewAddDTO;
import com.softuni.angelovestates.model.entity.Review;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.repository.ReviewRepository;
import com.softuni.angelovestates.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.AssertTrue;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository mockReviewRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private ModelMapper mockModelMapper;

    @Captor
    private ArgumentCaptor<Review> reviewArgumentCaptor;

    private ReviewService toTest;

    @BeforeEach
    void setUp() {
        toTest = new ReviewService(mockReviewRepository, mockUserRepository, mockModelMapper);
    }

    @Test
    public void testAddReview() {
        // Arrange
        String userEmail = "test@example.com";
        ReviewAddDTO reviewAddDTO = new ReviewAddDTO();
        User author = new User()
                .setId(1l)
                .setFirstName("James")
                .setLastName("Gosling")
                .setEmail(userEmail);

        when(mockUserRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(author));
        when(mockModelMapper.map(reviewAddDTO, Review.class)).thenReturn(new Review().setAuthor(author).setRating(5));

        // Act
        Review result = toTest.addReview(reviewAddDTO, userEmail);

        // Assert
        Mockito.verify(mockUserRepository).findUserByEmail(userEmail);
        Mockito.verify(mockReviewRepository).save(reviewArgumentCaptor.capture());

        assertEquals(author, result.getAuthor());
    }

    @Test
    public void testGetRandomReviews() {
        User author = new User()
                .setEmail("user@gmail.com")
                .setFirstName("James")
                .setLastName("Gosling");
        // Arrange
        List<Review> reviews = Arrays.asList(
                new Review().setRating(4).setAuthor(author),
                new Review().setRating(5).setAuthor(author),
                new Review().setRating(3).setAuthor(author),
                new Review().setRating(2).setAuthor(author)
        );

        when(mockReviewRepository.findAll()).thenReturn(reviews);

        // Act
        List<ReviewDAO> randomReviews = toTest.getRandomReviews();

        //Assert
        Mockito.verify(mockReviewRepository).findAll();
        assertEquals(3, randomReviews.size());
    }

    @Test
    public void testGetAverageRate() {
        // Arrange
        List<Review> reviews = Arrays.asList(
                new Review().setRating(4),
                new Review().setRating(5),
                new Review().setRating(3)
        );

        when(mockReviewRepository.findAll()).thenReturn(reviews);
        when(mockReviewRepository.count()).thenReturn((long) reviews.size());

        // Act
        double averageRate = toTest.getAverageRate();

        // Assert
        Mockito.verify(mockReviewRepository, times(1)).findAll();

        // The average rate is (4 + 5 + 3) / 3 = 4
        assertEquals(4.0, averageRate);
    }
}
