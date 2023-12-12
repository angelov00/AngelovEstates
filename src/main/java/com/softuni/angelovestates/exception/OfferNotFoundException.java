package com.softuni.angelovestates.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Offer was not found")
public class OfferNotFoundException extends RuntimeException {

    private final long offerId;

    public OfferNotFoundException(long offerId) {

        super("Offer with ID " + offerId + " not found!");

        this.offerId = offerId;
    }

    public long getOfferId() {
        return offerId;
    }
}
