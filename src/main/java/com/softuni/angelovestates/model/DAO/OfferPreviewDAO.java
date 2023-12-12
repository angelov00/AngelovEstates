package com.softuni.angelovestates.model.DAO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class OfferPreviewDAO {

    private long id;
    private String title;
    private String photoUrl;
    private String province;
    private BigDecimal price;
    private String offerType;
    private LocalDate listedOn;
    private double size;
}
