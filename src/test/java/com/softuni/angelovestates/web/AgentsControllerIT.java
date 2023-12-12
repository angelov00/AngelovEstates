package com.softuni.angelovestates.web;

import com.softuni.angelovestates.exception.AgentNotFoundException;
import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class AgentsControllerIT {

    @Mock
    private UserRepository mockUserRepository;

    private AgentsController agentsController;

    @BeforeEach
    void setUp() {
        agentsController = new AgentsController(mockUserRepository);
    }

    @Test
    public void testGetAgents() throws Exception {
        // Arrange test agents
        User firstAgent = new User()
                .setId(1L)
                .setFirstName("James")
                .setLastName("Gosling");

        User secondAgent = new User()
                .setId(2L)
                .setFirstName("Tommy")
                .setLastName("Vercetti");

        when(mockUserRepository.findUsersByIsAgentTrue()).thenReturn(Arrays.asList(firstAgent, secondAgent));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(agentsController).build();

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/agents"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("all-agents"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("agents"))
                .andExpect(MockMvcResultMatchers.model().attribute("agents", Arrays.asList(firstAgent, secondAgent)));
    }

    @Test
    public void testGetAgentDetails() throws Exception {
        // Arrange
        long agentId = 1L;
        User agent = new User().setId(agentId).setFirstName("James");

        when(mockUserRepository.findById(agentId)).thenReturn(Optional.of(agent));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(agentsController).build();

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/agents/details/{id}", agentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("agent-details"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("agent"))
                .andExpect(MockMvcResultMatchers.model().attribute("agent", agent));
    }

    @Test
    public void testGetAgentDetailsAgentNotFound() throws Exception {
        // Arrange
        long nonExistingAgentId = 1L;

        when(mockUserRepository.findById(nonExistingAgentId)).thenReturn(Optional.empty());
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(agentsController).build();

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/agents/details/{id}", nonExistingAgentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error-page"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", "Agent with id " + nonExistingAgentId + " not found"));
    }
}
