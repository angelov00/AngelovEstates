package com.softuni.angelovestates.web;

import com.softuni.angelovestates.model.DAO.OfferDetailsDAO;
import com.softuni.angelovestates.model.DTO.OfferAddDTO;
import com.softuni.angelovestates.service.OfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService mockOfferService;

    @Autowired
    private ModelMapper modelMapper;

    private OfferController offerController;

    @BeforeEach
    void setUp() {
        offerController = new OfferController(mockOfferService);
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    public void testGetAddOffer() throws Exception {
        mockMvc.perform(get("/offers/add")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("add-offer"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    public void testPostAddOfferWithValidInput() throws Exception {
        //Arrange
        OfferAddDTO offerAddDTO = new OfferAddDTO()
                .setOfferType("FOR_SALE")
                .setProvince("LOVECH")
                .setPrice(2000)
                .setAddress("test_address")
                .setPhotos(List.of())
                .setSize(4343)
                .setTitle("title")
                .setDescription("testtest")
                .setRooms(4);

        // Act & Assert
        mockMvc.perform(post("/offers/add").with(csrf())
                        .flashAttr("offerAddDTO", offerAddDTO)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testGetSearch() throws Exception {
        mockMvc.perform(get("/offers/search")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("search-offer"));
    }

    @Test
    public void testGetOfferById() throws Exception {
        long offerId = 1L;
        OfferDetailsDAO offerDetailsDAO = new OfferDetailsDAO();
        when(mockOfferService.getDetails(offerId)).thenReturn(offerDetailsDAO);

        mockMvc.perform(get("/offers/details/{id}", offerId)
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("offer-details"))
                .andExpect(model().attributeExists("offer"))
                .andExpect(model().attribute("offer", offerDetailsDAO));
    }

    @Test
    public void testGetNonExistentOfferById() throws Exception {

        long nonExistingOfferId = 1;
        when(mockOfferService.getDetails(nonExistingOfferId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/offers/details/{id}", nonExistingOfferId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error-page"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", "Offer with id " + nonExistingOfferId + " not found"));

    }

    @Test
    public void testDeleteOffer() throws Exception {
        long offerId = 1L; //Already existing offer

        // Act & Assert
        mockMvc.perform(get("/offers/delete/{id}", offerId)
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    public void testMakeActive() throws Exception {
        long offerId = 1L; //Already existing user

        // Act & Assert
        mockMvc.perform(get("/offers/makeActive/{id}", offerId)
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
