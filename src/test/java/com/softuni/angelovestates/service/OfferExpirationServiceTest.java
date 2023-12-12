package com.softuni.angelovestates.service;

import com.softuni.angelovestates.model.entity.Offer;
import com.softuni.angelovestates.repository.OfferRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OfferExpirationServiceTest {

    private OfferExpirationService toTest;

    @Mock
    private OfferRepository mockOfferRepository;

    @BeforeEach
    void setUp(){
        toTest = new OfferExpirationService(mockOfferRepository);
    }

    @Test
    public void testCheckAndDeactivateExpiredOffers() {
        // Arrange
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);

        // Mocking data for expired offers
        Offer expiredOffer1 = new Offer();
        expiredOffer1.setIsExpired(false);
        expiredOffer1.setListedOn(thirtyDaysAgo.minusDays(1));

        Offer expiredOffer2 = new Offer();
        expiredOffer2.setIsExpired(false);
        expiredOffer2.setListedOn(thirtyDaysAgo.minusDays(2));

        List<Offer> expiredOffers = Arrays.asList(expiredOffer1, expiredOffer2);

        // Mocking the repository method
        when(mockOfferRepository.findAllByIsExpiredAndListedOnBefore(false, thirtyDaysAgo))
                .thenReturn(expiredOffers);

        // Act
        toTest.checkAndDeactivateExpiredOffers();

        // Assert
        Mockito.verify(mockOfferRepository, times(1)).save(expiredOffer1);
        Mockito.verify(mockOfferRepository, times(1)).save(expiredOffer2);

        Assertions.assertTrue(expiredOffer1.getIsExpired());
        Assertions.assertTrue(expiredOffer2.getIsExpired());
    }
}
