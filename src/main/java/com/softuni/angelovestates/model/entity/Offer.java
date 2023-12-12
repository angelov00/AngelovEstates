package com.softuni.angelovestates.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false)
    private OfferType offerType;

    @ElementCollection
    private List<String> photoURLs;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @ManyToOne(optional = false)
    private Province province;

    @Column(nullable = false)
    private double size;

    @Column(nullable = false)
    private int rooms;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "listed_on")
    private LocalDate listedOn;

    @Column(name = "is_expired")
    private Boolean isExpired;

    @ManyToOne(targetEntity = User.class)
    private User seller;

    public Offer() {
        this.photoURLs = new ArrayList<>();
    }
}
