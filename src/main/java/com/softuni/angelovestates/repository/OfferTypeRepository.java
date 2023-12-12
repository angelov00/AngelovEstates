package com.softuni.angelovestates.repository;

import com.softuni.angelovestates.model.entity.OfferType;
import com.softuni.angelovestates.model.enums.OfferTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferTypeRepository extends JpaRepository<OfferType, Long> {

    OfferType findByOfferType(OfferTypeEnum type);
}
