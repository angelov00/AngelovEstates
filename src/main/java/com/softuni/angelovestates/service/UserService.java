package com.softuni.angelovestates.service;

import com.softuni.angelovestates.model.DAO.UserDetailsDAO;
import com.softuni.angelovestates.model.DAO.UserUpdateDAO;
import com.softuni.angelovestates.model.entity.Offer;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.model.enums.RoleEnum;
import com.softuni.angelovestates.repository.OfferRepository;
import com.softuni.angelovestates.repository.RoleRepository;
import com.softuni.angelovestates.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, OfferRepository offerRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    public long getUsersCount() {
        return this.userRepository.count();
    }

    public long getAgentsCount() {
        return this.userRepository.findUsersByIsAgentTrue().size();
    }

    public Optional<User> userByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    public void updateUser(UserUpdateDAO updatedUser, User currentUser) {

        if(updatedUser.getEmail() != null) {
            currentUser.setEmail(updatedUser.getEmail());
        }

        if(updatedUser.getFirstName() != null) {
            currentUser.setFirstName(updatedUser.getFirstName());
        }

        if(updatedUser.getLastName() != null) {
            currentUser.setLastName(updatedUser.getLastName());
        }

        if(updatedUser.getPhoneNumber() != null) {
            currentUser.setPhoneNumber(updatedUser.getPhoneNumber());
        }

        userRepository.saveAndFlush(currentUser);
    }

    public List<Offer> getUserOffers(String email) {
        return this.offerRepository.findAllBySeller_Email(email);
    }


    public List<Offer> getUserInactiveOffers(String email) {
        return this.offerRepository.findAllByIsExpiredTrueAndSeller_Email(email);
    }

    public void promoteToAdmin(String email) {
        Optional<User> userByEmail = this.userRepository.findUserByEmail(email);
        if(userByEmail.isPresent()) {
            userByEmail.get().getRoles().add(this.roleRepository.getByRole(RoleEnum.ADMIN));
            this.userRepository.saveAndFlush(userByEmail.get());
        }
    }

    public UserDetailsDAO getUserDetails(String email) {
            User user = this.userRepository.findUserByEmail(email).get();
            UserDetailsDAO userDetailsDAO = this.modelMapper.map(user, UserDetailsDAO.class)
                    .setOffersCount(this.offerRepository.findAllBySeller_Email(user.getEmail()).size());
            return userDetailsDAO;
    }

}
