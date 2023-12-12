package com.softuni.angelovestates.web;

import com.softuni.angelovestates.service.OfferService;
import com.softuni.angelovestates.service.ReviewService;
import com.softuni.angelovestates.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIT {

    @Mock
    private UserService mockUserService;
    @Mock
    private OfferService mockOfferService;
    @Mock
    private ReviewService mockReviewService;

    private AdminController adminControllerToTest;

    @BeforeEach
    void setUp() {
        adminControllerToTest = new AdminController(mockUserService, mockOfferService, mockReviewService);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testGetAdminPanel() throws Exception {
        long expectedUserCount = 10L;
        long expectedAgentCount = 5L;
        long expectedOfferCount = 20L;
        long expectedOffersThisWeek = 7L;
        double expectedAverageRating = 4.5;

        when(mockUserService.getUsersCount()).thenReturn(expectedUserCount);
        when(mockUserService.getAgentsCount()).thenReturn(expectedAgentCount);
        when(mockOfferService.getOffersCount()).thenReturn(expectedOfferCount);
        when(mockOfferService.getOffersThisWeekCount()).thenReturn(expectedOffersThisWeek);
        when(mockReviewService.getAverageRate()).thenReturn(expectedAverageRating);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(adminControllerToTest).build();

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-panel"))
                .andExpect(model().attribute("userCount", expectedUserCount))
                .andExpect(model().attribute("agentCount", expectedAgentCount))
                .andExpect(model().attribute("offerCount", expectedOfferCount))
                .andExpect(model().attribute("offersThisWeek", expectedOffersThisWeek))
                .andExpect(model().attribute("averageRating", expectedAverageRating));

        Mockito.verify(mockUserService, times(1)).getUsersCount();
        Mockito.verify(mockUserService, times(1)).getAgentsCount();
        Mockito.verify(mockOfferService, times(1)).getOffersCount();
        Mockito.verify(mockReviewService, times(1)).getAverageRate();
        Mockito.verify(mockOfferService, times(1)).getOffersThisWeekCount();
    }
}
