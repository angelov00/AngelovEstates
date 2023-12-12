package com.softuni.angelovestates.config;

import com.softuni.angelovestates.model.DAO.OfferPreviewDAO;
import com.softuni.angelovestates.model.DTO.OfferAddDTO;
import com.softuni.angelovestates.model.DTO.UserRegisterDTO;
import com.softuni.angelovestates.model.entity.Offer;
import com.softuni.angelovestates.model.entity.OfferType;
import com.softuni.angelovestates.model.entity.Province;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.repository.OfferTypeRepository;
import com.softuni.angelovestates.repository.ProvinceRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

@Configuration
public class BeanConfiguration {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BeanConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        // OfferAddDTO -> Offer
        TypeMap<OfferAddDTO, Offer> offerAddDTOOfferTypeMap = modelMapper.createTypeMap(OfferAddDTO.class, Offer.class);
        Converter<Integer, BigDecimal> priceConverter = context -> BigDecimal.valueOf(context.getSource());
        offerAddDTOOfferTypeMap.addMappings(mapper -> mapper.using(priceConverter).map(OfferAddDTO::getPrice, Offer::setPrice));

        //UserRegisterDTO -> User
        TypeMap<UserRegisterDTO, User> userMap = modelMapper.createTypeMap(UserRegisterDTO.class, User.class);
        Converter<String, String> getPasswordConverter =  context -> passwordEncoder.encode(context.getSource());
        userMap.addMappings(mapper -> mapper.using(getPasswordConverter).map(UserRegisterDTO::getPassword, User::setPassword));

        //Offer -> OfferPreviewDAO
        TypeMap<Offer, OfferPreviewDAO> offerPreviewDAOTypeMap = modelMapper.createTypeMap(Offer.class, OfferPreviewDAO.class);
        Converter<OfferType, String> offerTypeConverter = context -> context.getSource().getOfferType().getDisplayValue();
        offerPreviewDAOTypeMap.addMappings(mapper -> mapper.using(offerTypeConverter).map(Offer::getOfferType, OfferPreviewDAO::setOfferType));
        Converter<Province, String> provinceConverter = context -> context.getSource().getProvince().getDispayValue();
        offerPreviewDAOTypeMap.addMappings(mapper -> mapper.using(provinceConverter).map(Offer::getProvince, OfferPreviewDAO::setProvince));
        Converter<List<String>, String> photoURLConverter = context -> context.getSource().get(0);
        offerPreviewDAOTypeMap.addMappings(mapper -> mapper.using(photoURLConverter).map(Offer::getPhotoURLs, OfferPreviewDAO::setPhotoUrl));

        return modelMapper;
    }

}
