package com.softuni.angelovestates.web;

import com.softuni.angelovestates.model.entity.User;
import com.softuni.angelovestates.exception.AgentNotFoundException;
import com.softuni.angelovestates.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/agents")
public class AgentsController {

    private final UserRepository userRepository;

    @Autowired
    public AgentsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping()
    public String getAgents(Model model) {
        List<User> agents = this.userRepository.findUsersByIsAgentTrue();
        model.addAttribute("agents", agents);
        return "all-agents";
    }

    @GetMapping("/details/{id}")
    public String getAgentDetails(@PathVariable long id, Model model) {

        Optional<User> user = this.userRepository.findById(id);
        if(user.isEmpty()) {
            throw new AgentNotFoundException(id);
        }
        model.addAttribute("agent", user.get());

        return "agent-details";
    }

    @ExceptionHandler(AgentNotFoundException.class)
    public String handleOfferNotFoundException(AgentNotFoundException ex, Model model) {
        model.addAttribute("error", "Agent with id " + ex.getOfferId() + " not found");
        return "error-page";
    }


}
