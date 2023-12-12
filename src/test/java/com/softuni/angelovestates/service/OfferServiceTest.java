package com.softuni.angelovestates.service;

import com.softuni.angelovestates.model.DAO.OfferDetailsDAO;
import com.softuni.angelovestates.model.DAO.OfferPreviewDAO;
import com.softuni.angelovestates.model.DAO.SellerDAO;
import com.softuni.angelovestates.model.DTO.OfferAddDTO;
import com.softuni.angelovestates.model.entity.Offer;
import com.softuni.angelovestates.model.entity.OfferType;
import com.softuni.angelovestates.model.entity.Province;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.model.enums.OfferTypeEnum;
import com.softuni.angelovestates.model.enums.ProvinceEnum;
import com.softuni.angelovestates.exception.OfferNotFoundException;
import com.softuni.angelovestates.repository.OfferRepository;
import com.softuni.angelovestates.repository.OfferTypeRepository;
import com.softuni.angelovestates.repository.ProvinceRepository;
import com.softuni.angelovestates.util.FileUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OfferServiceTest {

    @Captor
    private ArgumentCaptor<OfferAddDTO> offerAddDTOArgumentCaptor;

    @Captor
    private ArgumentCaptor<Offer> offerArgumentCaptor;

    @Mock
    private OfferRepository mockOfferRepository;
    @Mock
    private OfferTypeRepository mockOfferTypeRepository;
    @Mock
    private ProvinceRepository mockProvinceRepository;
    @Mock
    private ModelMapper mockModelMapper;
    @Mock
    private UserService mockUserService;
    @Mock
    private FileUploadService mockFileUploadService;


    private OfferService offerServiceToTest;

    @BeforeEach
    void setUp() {
        offerServiceToTest = new OfferService(mockOfferRepository,
                mockOfferTypeRepository,
                mockProvinceRepository,
                mockModelMapper,
                mockFileUploadService,
                mockUserService);
    }

    @Test
    public void testAddOffer() throws IOException {
        // Arrange
        OfferAddDTO offerAddDTO = new OfferAddDTO()
        .setOfferType(OfferTypeEnum.FOR_SALE.toString())
        .setPhotos(List.of())
        .setProvince(ProvinceEnum.LOVECH.toString());

        String email = "test@example.com";
        User seller = new User().setEmail(email);

        OfferType FOR_SALE = new OfferType().setOfferType(OfferTypeEnum.FOR_SALE);
        Province LOVECH = new Province().setProvince(ProvinceEnum.LOVECH);

        Offer map = new Offer()
                .setOfferType(new OfferType().setOfferType(OfferTypeEnum.FOR_SALE))
                .setProvince(new Province().setProvince(ProvinceEnum.LOVECH))
                .setPhotoURLs(new ArrayList<>());

        when(mockModelMapper.map(offerAddDTO, Offer.class)).thenReturn(map);
        when(mockOfferTypeRepository.findByOfferType(OfferTypeEnum.FOR_SALE)).thenReturn(FOR_SALE);
        when(mockUserService.userByEmail(email)).thenReturn(Optional.of(seller));
        lenient().when(mockProvinceRepository.getProvinceByProvince(ProvinceEnum.SOFIA_CITY)).thenReturn(LOVECH);

        // Act
        offerServiceToTest.addOffer(offerAddDTO, "test@example.com");

        // Assert
        // You can add more specific verifications based on your implementation
        Mockito.verify(mockModelMapper).map(offerAddDTO, Offer.class);
        Mockito.verify(mockOfferTypeRepository).findByOfferType(OfferTypeEnum.FOR_SALE);
        Mockito.verify(mockProvinceRepository).getProvinceByProvince(ProvinceEnum.LOVECH);
        Mockito.verify(mockFileUploadService).uploadOfferPhotos(any());
        Mockito.verify(mockUserService).userByEmail("test@example.com");
        Mockito.verify(mockOfferRepository).saveAndFlush(any(Offer.class));
    }

    @Test
    public void testGetDetails() {
//        // Arrange
//        long offerId = 1L;
//        Offer offer = new Offer();
//        offer.setId(offerId);
//        OfferType FOR_SALE = new OfferType().setOfferType(OfferTypeEnum.FOR_SALE);
//        offer.setOfferType(FOR_SALE);
//
//        OfferDetailsDAO result = new OfferDetailsDAO();
//
//        when(mockOfferRepository.findById(offerId)).thenReturn(Optional.of(offer));
//        lenient().when(mockModelMapper.map(Optional.of(offer), OfferDetailsDAO.class)).thenReturn(result);
//        // Act
//        OfferDetailsDAO offerDetailsDAO = offerServiceToTest.getDetails(offerId);
//
//        // Assert
//        assertNotNull(offerDetailsDAO);
//        // Add more specific assertions based on your implementation
        // Arrange
        long offerId = 1L;
        Offer mockOffer = new Offer();
        mockOffer.setId(offerId);
        OfferType offerType = new OfferType().setOfferType(OfferTypeEnum.FOR_SALE);
        Province province = new Province().setProvince(ProvinceEnum.DOBRICH);
        mockOffer.setOfferType(offerType);
        mockOffer.setProvince(province);

        User mockSeller = new User();
        mockSeller.setId(1L);
        mockOffer.setSeller(mockSeller);

        SellerDAO sellerDAO = new SellerDAO();
        OfferDetailsDAO offerDetailsDAO = new OfferDetailsDAO();

        when(mockOfferRepository.findById(offerId)).thenReturn(Optional.of(mockOffer));

        // Assuming the mapping works correctly
        when(mockModelMapper.map(mockOffer.getSeller(), SellerDAO.class)).thenReturn(sellerDAO);
        when(mockModelMapper.map(mockOffer, OfferDetailsDAO.class)).thenReturn(offerDetailsDAO);

        // Act
        OfferDetailsDAO result = offerServiceToTest.getDetails(offerId);

        // Assert
        assertNotNull(result);
        // Add more specific assertions based on your implementation
        // Verify that the OfferRepository's findById was called
        //TODO
        verify(mockOfferRepository, times(1)).findById(offerId);
    }

    @Test
    public void testGetCount() {
        long expectedCount = 10L;
        when(mockOfferRepository.count()).thenReturn(expectedCount);

        assertEquals(offerServiceToTest.getOffersCount(), expectedCount);
    }

    @Test
    public void testDeleteOfferByID() {
        // Arrange
        long offerId = 1L;

        // Act
        offerServiceToTest.deleteOfferByID(offerId);

        // Assert
        Mockito.verify(mockOfferRepository, times(1)).deleteById(offerId);
    }

    @Test
    public void testGetOffersThisWeekCount() {
        // Arrange
        when(mockOfferRepository.findAllByListedOnAfter(any(LocalDate.class))).thenReturn(Arrays.asList(new Offer(), new Offer()));

        // Act
        long count = offerServiceToTest.getOffersThisWeekCount();

        // Assert
        assertEquals(2, count);
    }

    @Test
    public void testMakeActive() {
        // Arrange
        long offerId = 1L;
        Offer offer = new Offer();
        offer.setId(offerId);
        when(mockOfferRepository.findById(offerId)).thenReturn(Optional.of(offer));

        // Act
        offerServiceToTest.makeActive(offerId);

        // Assert
        assertFalse(offer.getIsExpired());
        Mockito.verify(mockOfferRepository, times(1)).saveAndFlush(offer);
    }

    @Test
    public void testMakeActiveOfferNotFound() {
        // Arrange
        long offerId = 1L;
        when(mockOfferRepository.findById(offerId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(OfferNotFoundException.class, () -> offerServiceToTest.makeActive(offerId));
    }

    @Test
    public void testGetUserActiveOffers() {
        // Arrange
        String userEmail = "test@example.com";
        when(mockOfferRepository.findAllByIsExpiredFalseAndSeller_Email(userEmail)).thenReturn(Arrays.asList(new Offer(), new Offer()));

        // Act
        List<OfferPreviewDAO> result = offerServiceToTest.getUserActiveOffers(userEmail);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testGetUserExpiredOffers() {
        // Arrange
        String userEmail = "test@example.com";
        when(mockOfferRepository.findAllByIsExpiredTrueAndSeller_Email(userEmail)).thenReturn(Arrays.asList(new Offer(), new Offer()));

        // Act
        List<OfferPreviewDAO> result = offerServiceToTest.getUserExpiredOffers(userEmail);

        // Assert
        assertEquals(2, result.size());
    }

}
