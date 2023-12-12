package com.softuni.angelovestates.service;

import com.softuni.angelovestates.model.DTO.UserRegisterDTO;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.repository.RoleRepository;
import com.softuni.angelovestates.repository.UserRepository;
import com.softuni.angelovestates.util.EmailSender;
import com.softuni.angelovestates.util.FileUploadService;
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

import java.io.IOException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ModelMapper mockModelMapper;
    @Mock
    private FileUploadService mockFileUploadService;
    @Mock
    private EmailSender mockEmailSender;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    private AuthService toTest;

    @BeforeEach
    void setUp() {
        toTest = new AuthService(mockUserRepository, mockRoleRepository, mockFileUploadService, mockModelMapper, mockEmailSender);
    }

    @Test
    void testRegistration() throws IOException {
        //arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO()
                .setEmail("test@abv.bg")
                .setFirstName("John")
                .setLastName("Gates")
                .setPassword("topsecret")
                .setPhoneNumber("07777777")
                .setAgent(false)
                .setCompanyName("");

        when(mockModelMapper.map(userRegisterDTO, User.class))
                .thenReturn(new User().setEmail("test@abv.bg")
                        .setFirstName("John")
                        .setLastName("Gates")
                        .setPassword("encoded_password")
                        .setPhoneNumber("07777777")
                        .setAgent(false)
                        .setCompany(""));

        //act
        toTest.register(userRegisterDTO);

        //assert

        Mockito.verify(mockUserRepository).saveAndFlush(userArgumentCaptor.capture());

        User actualUser = userArgumentCaptor.getValue();
        Assertions.assertEquals(userRegisterDTO.getEmail(), actualUser.getEmail());
        Assertions.assertEquals("encoded_password", actualUser.getPassword());

    }

}
