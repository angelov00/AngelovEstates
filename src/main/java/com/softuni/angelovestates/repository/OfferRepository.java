package com.softuni.angelovestates.repository;

import com.softuni.angelovestates.model.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {

    List<Offer> findAllBySeller_Email(String email);
    List<Offer> findAllByIsExpiredAndListedOnBefore(Boolean bool, LocalDate before);
    List<Offer> findAllByIsExpiredFalseAndSeller_Email(String email);
    List<Offer> findAllByIsExpiredTrueAndSeller_Email(String email);
    List<Offer> findAllByListedOnAfter(LocalDate weekBefore);
}
