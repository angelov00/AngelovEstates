package com.softuni.angelovestates.repository;

import com.softuni.angelovestates.model.DTO.OfferSearchDTO;
import com.softuni.angelovestates.model.entity.Offer;
import com.softuni.angelovestates.model.enums.OfferTypeEnum;
import com.softuni.angelovestates.model.enums.ProvinceEnum;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class OfferSpecification implements Specification<Offer> {

    private final OfferSearchDTO offerSearchDTO;

    public OfferSpecification(OfferSearchDTO offerSearchDTO) {
        this.offerSearchDTO = offerSearchDTO;
    }

    @Override
    public Predicate toPredicate(Root<Offer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Specification<Offer> where = Specification.where(offerNotExpired());

        if(offerSearchDTO.getMinPrice() != null) {
            where = where.and(priceGreaterOrEqualThan(offerSearchDTO.getMinPrice()));
        }

        if(offerSearchDTO.getMaxPrice() != null) {
            where = where.and(priceLessOrEqualThan(offerSearchDTO.getMaxPrice()));
        }

        if(offerSearchDTO.getMinSize() != null) {
            where = where.and(sizeGreaterOrEqualThan(offerSearchDTO.getMinSize()));
        }

        if(offerSearchDTO.getMaxSize() != null) {
            where = where.and(sizeLessOrEqualThan(offerSearchDTO.getMaxSize()));
        }

        if(offerSearchDTO.getProvince() != null && !offerSearchDTO.getProvince().isEmpty()) {
            where = where.and(offerProvinceEqualsTo(offerSearchDTO.getProvince()));
        }

        if(offerSearchDTO.getOfferType() != null && !offerSearchDTO.getOfferType().isEmpty()) {
            where = where.and(offerTypeEqualsTo(offerSearchDTO.getOfferType()));
        }




        return where.toPredicate(root, query, criteriaBuilder);
    }

    @Override
    public Specification<Offer> and(Specification<Offer> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Offer> or(Specification<Offer> other) {
        return Specification.super.or(other);
    }

    private static Specification<Offer> priceGreaterOrEqualThan(int minPrice) {
        return (r, q, b) -> b.and(b.greaterThanOrEqualTo(r.get("price"), minPrice));
    }

    private static Specification<Offer> priceLessOrEqualThan(int maxPrice) {
        return (r,q,b)-> b.and(b.lessThanOrEqualTo(r.get("price"), maxPrice));
    }

    private static Specification<Offer> sizeGreaterOrEqualThan(int minSize) {
        return (r, q, b) -> b.and(b.greaterThanOrEqualTo(r.get("size"), minSize));
    }

    private static Specification<Offer> sizeLessOrEqualThan(int maxSize) {
        return (r,q,b)-> b.and(b.lessThanOrEqualTo(r.get("size"), maxSize));
    }

    private static Specification<Offer> offerProvinceEqualsTo(String province) {
        return (r, q, b) -> b.and(b.equal(r.get("province").get("province"), ProvinceEnum.valueOf(province)));
    }

    private static Specification<Offer> offerTypeEqualsTo(String offerType) {
        return (r, q, b) -> b.and(b.equal(r.get("offerType").get("offerType"), OfferTypeEnum.valueOf(offerType)));
    }

    public static Specification<Offer> offerNotExpired() {
        return (r, q, b) -> b.isFalse(r.get("isExpired"));
    }


}
