package com.softuni.angelovestates.service;

import com.softuni.angelovestates.model.DAO.UserDetailsDAO;
import com.softuni.angelovestates.model.DAO.UserUpdateDAO;
import com.softuni.angelovestates.model.entity.Offer;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.model.enums.RoleEnum;
import com.softuni.angelovestates.repository.OfferRepository;
import com.softuni.angelovestates.repository.RoleRepository;
import com.softuni.angelovestates.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private OfferRepository mockOfferRepository;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private ModelMapper mockModelMapper;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(mockUserRepository, mockOfferRepository, mockRoleRepository, mockModelMapper);
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        UserUpdateDAO updatedUser = new UserUpdateDAO();
        updatedUser.setEmail("newemail@example.com");

        User currentUser = new User();
        currentUser.setEmail("oldemail@example.com");

        // Act
        userService.updateUser(updatedUser, currentUser);

        // Assert
        assertEquals("newemail@example.com", currentUser.getEmail());
        Mockito.verify(mockUserRepository).saveAndFlush(currentUser);
    }

    @Test
    public void testGetUserOffers() {
        // Arrange
        String userEmail = "test@example.com";
        when(mockOfferRepository.findAllBySeller_Email(userEmail)).thenReturn(Collections.singletonList(new Offer()));

        // Act
        List<Offer> result = userService.getUserOffers(userEmail);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    public void testUserByEmail() {
        // Arrange
        String userEmail = "test@example.com";
        when(mockUserRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(new User()));

        // Act
        Optional<User> result = userService.userByEmail(userEmail);

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    public void testUserByEmailNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        when(mockUserRepository.findUserByEmail(userEmail)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.userByEmail(userEmail);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetUserInactiveOffers() {
        // Arrange
        String userEmail = "test@example.com";
        when(mockOfferRepository.findAllByIsExpiredTrueAndSeller_Email(userEmail)).thenReturn(Collections.singletonList(new Offer()));

        // Act
        List<Offer> result = userService.getUserInactiveOffers(userEmail);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    public void testPromoteToAdmin() {
        // Arrange
        String userEmail = "test@example.com";
        when(mockUserRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(new User()));
       // when(mockRoleRepository.getByRole(RoleEnum.ADMIN)).thenReturn();

        // Act
        userService.promoteToAdmin(userEmail);

        // Assert
        Mockito.verify(mockUserRepository, times(1)).saveAndFlush(userArgumentCaptor.capture());
    }

    @Test
    public void testGetUserDetails() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);
        when(mockUserRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(user));
        when(mockOfferRepository.findAllBySeller_Email(userEmail)).thenReturn(Collections.singletonList(new Offer()));
        when(mockModelMapper.map(user, UserDetailsDAO.class)).thenReturn(new UserDetailsDAO());

        // Act
        UserDetailsDAO result = userService.getUserDetails(userEmail);

        // Assert
        Assertions.assertNotNull(result);
        assertEquals(1, result.getOffersCount());
    }

    @Test
    public void testGetUsersCount() {
        // Arrange
        when(mockUserRepository.count()).thenReturn(5L);

        // Act
        long result = userService.getUsersCount();

        // Assert
        assertEquals(5, result);
    }

    @Test
    public void testGetAgentsCount() {
        // Arrange
        when(mockUserRepository.findUsersByIsAgentTrue()).thenReturn(Arrays.asList(new User(), new User()));

        // Act
        long result = userService.getAgentsCount();

        // Assert
        assertEquals(2, result);
    }
}
