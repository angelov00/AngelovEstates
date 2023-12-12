package com.softuni.angelovestates.web;

import com.softuni.angelovestates.model.DAO.OfferPreviewDAO;
import com.softuni.angelovestates.service.OfferService;
import com.softuni.angelovestates.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;
    @MockBean
    private OfferService mockOfferService;

    private UserController userControllerToTest;

    @BeforeEach
    void setUp() {
        userControllerToTest = new UserController(mockUserService, mockOfferService);
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    public void testGetMyOffers() throws Exception {

        List<OfferPreviewDAO> activeOffers = new ArrayList<>();
        List<OfferPreviewDAO> expiredOffers = new ArrayList<>();
        when(mockOfferService.getUserActiveOffers("testUser")).thenReturn(activeOffers);
        when(mockOfferService.getUserExpiredOffers("testUser")).thenReturn(expiredOffers);

        mockMvc.perform(get("/user/offers")
                        .contentType(MediaType.TEXT_HTML)
                        .principal(() -> "user@gmail.com")) // Simulating a user authentication
                .andExpect(status().isOk())
                .andExpect(view().name("user-offers"))
                .andExpect(model().attribute("offers", activeOffers))
                .andExpect(model().attribute("expiredOffers", expiredOffers));
    }
}
