package com.softuni.angelovestates.service;

import com.softuni.angelovestates.model.entity.Role;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.model.enums.RoleEnum;
import com.softuni.angelovestates.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.AssertionFailedError;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserDetailsServiceTest {

    private ApplicationUserDetailsService toTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        toTest = new ApplicationUserDetailsService(mockUserRepository);
    }

    @Test
    void testUserFound() {

        //ARRANGE
        Role userRole = new Role().setRole(RoleEnum.USER);
        Role adminRole = new Role().setRole(RoleEnum.ADMIN);
        User testUser = new User().setEmail("admin@gmail.com").setPassword("StrongPassword").setRoles(Set.of(userRole, adminRole));

        when(mockUserRepository.findUserByEmail("admin@gmail.com")).thenReturn(Optional.of(testUser));

        //ACT
        UserDetails adminDetails = toTest.loadUserByUsername("admin@gmail.com");

        //ASSERT
        Assertions.assertNotNull(adminDetails);
        Assertions.assertEquals("admin@gmail.com", adminDetails.getUsername());
        Assertions.assertEquals("StrongPassword", adminDetails.getPassword());
        Assertions.assertEquals(2, adminDetails.getAuthorities().size());
        assertRole(adminDetails.getAuthorities(), "ROLE_ADMIN");
        assertRole(adminDetails.getAuthorities(), "ROLE_USER");
    }

    @Test
    void testUserNotFound() {
        assertThrows(UsernameNotFoundException.class,
                () -> toTest.loadUserByUsername("non-existent@gmail.com"));
    }

    private void assertRole(Collection<? extends GrantedAuthority> authorities, String role) {
        authorities
                .stream()
                .filter(a -> role.equals(a.getAuthority()))
                .findAny()
                .orElseThrow(() -> new AssertionFailedError("Role " + role +" not found."));
    }

}
