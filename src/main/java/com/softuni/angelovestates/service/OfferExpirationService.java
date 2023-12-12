package com.softuni.angelovestates.service;

import com.softuni.angelovestates.model.entity.Offer;
import com.softuni.angelovestates.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OfferExpirationService {

    private final OfferRepository offerRepository;

    @Autowired
    public OfferExpirationService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAndDeactivateExpiredOffers() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);

        List<Offer> expiredOffers = offerRepository.findAllByIsExpiredAndListedOnBefore(false, thirtyDaysAgo);

        for (Offer offer : expiredOffers) {
            offer.setIsExpired(true);
            offerRepository.save(offer);
        }
    }

}
