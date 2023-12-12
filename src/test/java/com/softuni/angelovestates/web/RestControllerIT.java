package com.softuni.angelovestates.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softuni.angelovestates.model.DAO.ReviewDAO;
import com.softuni.angelovestates.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class RestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService mockReviewService;

    private ReviewRESTController restControllerToTest;

    @BeforeEach
    void setUp() {
        restControllerToTest = new ReviewRESTController(mockReviewService);
    }
    @Test
    public void testGetRandomReviews() throws Exception {
        // Arrange
        List<ReviewDAO> expectedReviews = Arrays.asList(
                new ReviewDAO().setAuthor("User1").setRating(4).setComment("Good service"),
                new ReviewDAO().setAuthor("User2").setRating(4).setComment("Good service")
        );

        when(mockReviewService.getRandomReviews()).thenReturn(expectedReviews);

        // Assert
        mockMvc.perform(get("/api/reviews/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].author").value("User1"))
                .andExpect(jsonPath("$[0].rating").value(4))
                .andExpect(jsonPath("$[1].author").value("User2"))
                .andExpect(jsonPath("$[1].rating").value(4))
                .andReturn();
    }

}
