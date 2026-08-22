package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.User;
import com.legalcontract.legal_contract_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService
{

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    // CREATE
    public User createUser(User user)
    {
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
        user.setPassword(userDetails.getPassword());

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