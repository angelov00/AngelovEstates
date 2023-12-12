package com.softuni.angelovestates.web;

import com.softuni.angelovestates.model.DTO.ReviewAddDTO;
import com.softuni.angelovestates.model.entity.Offer;
import com.softuni.angelovestates.model.enums.OfferTypeEnum;
import com.softuni.angelovestates.model.enums.ProvinceEnum;
import com.softuni.angelovestates.service.OfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.instanceOf;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService mockOfferService;

    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController(mockOfferService);
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    public void testGetHome() throws Exception {
        // Arrange
        long expectedOffersCount = 10;
        when(mockOfferService.getOffersCount()).thenReturn(expectedOffersCount);

        // Act & Assert
        mockMvc.perform(get("/")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("offersCount", expectedOffersCount))
                .andExpect(model().attribute("reviewAddDTO", instanceOf(ReviewAddDTO.class)));
    }

    @Test
    public void testGetAboutUs() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/about-us")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("about-us"));
    }

    @Test
    public void testGetContacts() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/contacts")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("contacts"));
    }

}
