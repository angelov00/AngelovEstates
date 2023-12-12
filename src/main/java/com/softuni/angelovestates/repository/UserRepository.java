package com.softuni.angelovestates.repository;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.softuni.angelovestates.model.entity.Role;
import com.softuni.angelovestates.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);
    List<User> findUsersByIsAgentTrue();
}
