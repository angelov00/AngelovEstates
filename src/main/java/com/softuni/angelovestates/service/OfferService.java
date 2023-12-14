package com.softuni.angelovestates.service;

import com.softuni.angelovestates.model.DAO.OfferDetailsDAO;
import com.softuni.angelovestates.model.DAO.OfferPreviewDAO;
import com.softuni.angelovestates.model.DAO.SellerDAO;
import com.softuni.angelovestates.model.DTO.OfferAddDTO;
import com.softuni.angelovestates.model.DTO.OfferSearchDTO;
import com.softuni.angelovestates.model.entity.Offer;
import com.softuni.angelovestates.model.enums.OfferTypeEnum;
import com.softuni.angelovestates.model.enums.ProvinceEnum;
import com.softuni.angelovestates.exception.OfferNotFoundException;
import com.softuni.angelovestates.repository.OfferRepository;
import com.softuni.angelovestates.repository.OfferSpecification;
import com.softuni.angelovestates.repository.OfferTypeRepository;
import com.softuni.angelovestates.repository.ProvinceRepository;
import com.softuni.angelovestates.util.FileUploadService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferTypeRepository offerTypeRepository;
    private final ProvinceRepository provinceRepository;
    private final ModelMapper modelMapper;
    private final FileUploadService fileUploadService;
    private final UserService userService;

    @Autowired
    public OfferService(OfferRepository offerRepository, OfferTypeRepository offerTypeRepository, ProvinceRepository provinceRepository, ModelMapper modelMapper, FileUploadService fileUploadService, UserService userService) {
        this.offerRepository = offerRepository;
        this.offerTypeRepository = offerTypeRepository;
        this.provinceRepository = provinceRepository;
        this.modelMapper = modelMapper;
        this.fileUploadService = fileUploadService;
        this.userService = userService;
    }

    public long getOffersCount() {
        return this.offerRepository.count();
    }

    public void addOffer(OfferAddDTO offerAddDTO0, String email) throws IOException {

        Offer offer =
                this.modelMapper.map(offerAddDTO0, Offer.class)
                .setOfferType(this.offerTypeRepository.findByOfferType(offerAddDTO0.getOfferType()))
                .setIsExpired(false)
                .setPhotoURLs(this.fileUploadService.uploadOfferPhotos(offerAddDTO0.getPhotos()))
                .setListedOn(LocalDate.now())
                .setProvince(this.provinceRepository.getProvinceByProvince(ProvinceEnum.valueOf(offerAddDTO0.getProvince())))
                .setSeller(this.userService.userByEmail(email).get());

        this.offerRepository.saveAndFlush(offer);
    }

    public Page<OfferPreviewDAO> findOffers(OfferSearchDTO filter, Pageable pageable) {
        return this.offerRepository
                .findAll(new OfferSpecification(filter), pageable)
                .map(this::mapOfferPreviewDAO);
    }

    public OfferDetailsDAO getDetails(long id) {

        Optional<Offer> offer = this.offerRepository.findById(id);

        if (offer.isEmpty()) {
            return null;
        } else {
            SellerDAO sellerDAO = this.modelMapper.map(offer.get().getSeller(), SellerDAO.class);
            return this.modelMapper.map(offer.get(), OfferDetailsDAO.class)
                    .setOfferType(offer.get().getOfferType().getOfferType().getDisplayValue())
                    .setProvince(offer.get().getProvince().getProvince().getDispayValue())
                    .setSeller(sellerDAO);
        }
    }

    public void deleteOfferByID(long id) {
        this.offerRepository.deleteById(id);
    }

    public long getOffersThisWeekCount() {
        LocalDate weekAgo = LocalDate.now().minusWeeks(1);
        return this.offerRepository.findAllByListedOnAfter(weekAgo).size();
    }

    public void makeActive(long id) {
        Optional<Offer> offerById = this.offerRepository.findById(id);
        if(offerById.isPresent()) {
            offerById.get().setIsExpired(false);
            this.offerRepository.saveAndFlush(offerById.get());
        } else {
            throw new OfferNotFoundException(id);
        }

    }

    public List<OfferPreviewDAO> getUserActiveOffers(String email) {
        return this.offerRepository
                .findAllByIsExpiredFalseAndSeller_Email(email)
                .stream()
                .map(this::mapOfferPreviewDAO)
                .collect(Collectors.toList());
    }

    public List<OfferPreviewDAO> getUserExpiredOffers(String email) {
        return this.offerRepository
                .findAllByIsExpiredTrueAndSeller_Email(email)
                .stream()
                .map(this::mapOfferPreviewDAO)
                .collect(Collectors.toList());
    }

    private OfferPreviewDAO mapOfferPreviewDAO(Offer offer) {
        return this.modelMapper.map(offer, OfferPreviewDAO.class);
    }
    
    public boolean isOfferOwner(Authentication authentication, long offerId) {

        String authUsername = authentication.getName();
        List<Offer> offers = this.offerRepository.findAllBySeller_Email(authUsername);
        return offers.stream().anyMatch(offer -> offer.getId() == offerId);

    }
}
