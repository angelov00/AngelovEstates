package com.softuni.angelovestates.web;

import com.softuni.angelovestates.model.DTO.UserLoginDTO;
import com.softuni.angelovestates.model.DTO.UserRegisterDTO;
import com.softuni.angelovestates.model.entity.Role;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.model.enums.RoleEnum;
import com.softuni.angelovestates.repository.RoleRepository;
import com.softuni.angelovestates.repository.UserRepository;
import com.softuni.angelovestates.service.AuthService;
import com.softuni.angelovestates.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.data.repository.util.ClassUtils.hasProperty;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.thymeleaf.spring5.util.FieldUtils.hasErrors;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIT {

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AuthService mockAuthService;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private UserService mockUserService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(mockAuthService, mockUserService);
    }

    @Test
    public void testGetLogin() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/auth/login")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testPostLoginWithValidInput() throws Exception {
        //Arrange -> The User is already arranged in InitDB file

        // Act & Assert
        mockMvc.perform(post("/auth/login").with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "user@gmail.com")
                        .param("password", "user123")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testPostLoginWithInvalidInput() throws Exception {

        // Act & Assert
        mockMvc.perform(post("/auth/login").with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "userNotExist@gmail.com")
                        .param("password", "user123")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }


    @Test
    public void testGetRegister() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/auth/register")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void testPostRegisterWithValidInput() throws Exception {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("newuser@example.com");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setConfirmPassword("password");
        userRegisterDTO.setCompanyName("");
        userRegisterDTO.setAgent(false);

        when(mockUserService.userByEmail("newuser@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf())
                        .param("email", "newuser@example.com")
                        .param("firstName", "Admin")
                        .param("lastName", "Adminov")
                        .param("phoneNumber", "08349393")
                        .param("password", "password")
                        .param("confirmPassword", "password")
                        .param("companyName", "")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("login"));
    }

    @Test
    public void testPostRegisterWithValidData() throws Exception {
        // Prepare a valid user registration DTO
        UserRegisterDTO validUserRegisterDTO = new UserRegisterDTO()
                .setEmail("test@example.com")
                .setPhoneNumber("087 543 3535")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("password")
                .setConfirmPassword("password")
                .setCompanyName("");

        // Perform the POST request
        mockMvc.perform(post("/auth/register").with(csrf())
                        .param("email", validUserRegisterDTO.getEmail())
                        .param("firstName", validUserRegisterDTO.getFirstName())
                        .param("lastName", validUserRegisterDTO.getLastName())
                        .param("phoneNumber", validUserRegisterDTO.getPhoneNumber())
                        .param("companyName", validUserRegisterDTO.getCompanyName())
                        .param("password", validUserRegisterDTO.getPassword())
                        .param("confirmPassword", validUserRegisterDTO.getConfirmPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("login"));
    }

    @Test
    public void testPostRegisterWithInvalidData() throws Exception {
        // Prepare an invalid user registration DTO
        UserRegisterDTO invalidUserRegisterDTO = new UserRegisterDTO();
        invalidUserRegisterDTO.setEmail("invalid-email");
        invalidUserRegisterDTO.setPassword("pas");

        // Mock the userRepository to return an empty Optional, indicating the user does not exist
        when(mockUserRepository.findUserByEmail(stringArgumentCaptor.capture())).thenReturn(Optional.empty());

        // Perform the POST request
        mockMvc.perform(post("/auth/register").with(csrf())
                        .param("email", invalidUserRegisterDTO.getEmail())
                        .param("password", invalidUserRegisterDTO.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("register"));
    }

}
