package com.softuni.angelovestates.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Agent was not found")
public class AgentNotFoundException extends RuntimeException {

    private final long offerId;

    public AgentNotFoundException(long offerId) {

        super("Agent with ID " + offerId + " not found!");

        this.offerId = offerId;
    }

    public long getOfferId() {
        return offerId;
    }
}
