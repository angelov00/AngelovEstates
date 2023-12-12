package com.softuni.angelovestates.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class OfferSearchDTO {

    @Positive
    private Integer minPrice;
    @Positive
    private Integer maxPrice;
    private String province;
    @Positive
    private Integer minSize;
    @Positive
    private Integer maxSize;
    @Positive
    private Integer minRooms;
    @Positive
    private Integer maxRooms;
    private String offerType;
}
