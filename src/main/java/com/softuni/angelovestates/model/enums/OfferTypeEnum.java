package com.softuni.angelovestates.model.enums;

public enum OfferTypeEnum {
    FOR_SALE("For Sale"),
    TO_RENT("To Rent");

    private final String displayValue;

    OfferTypeEnum(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
