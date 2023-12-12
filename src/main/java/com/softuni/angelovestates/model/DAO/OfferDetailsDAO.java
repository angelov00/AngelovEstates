package com.softuni.angelovestates.model.DAO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class OfferDetailsDAO {

    private String title;
    private String offerType;
    private BigDecimal price;
    private double size;
    private int rooms;
    private String province;
    private String address;
    private List<String> photoURLs;
    private String description;
    private LocalDate listedOn;
    private SellerDAO seller;
}
