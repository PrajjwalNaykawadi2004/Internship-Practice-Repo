package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.User;
import com.legalcontract.legal_contract_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService
{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    public User createUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // READ ALL
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    // READ BY ID
    public Optional<User> getUserById(Long id)
    {
        return userRepository.findById(id);
    }

    // UPDATE
    public User updateUser(Long id, User userDetails)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        user.setRole(userDetails.getRole());

        return userRepository.save(user);
    }

    // DELETE
    public void deleteUser(Long id)
    {
        if (!userRepository.existsById(id))
        {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }
}