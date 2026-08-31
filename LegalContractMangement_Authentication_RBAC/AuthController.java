package com.legalcontract.legal_contract_management_system.controller;

import com.legalcontract.legal_contract_management_system.dto.LoginRequest;
import com.legalcontract.legal_contract_management_system.entity.User;
import com.legalcontract.legal_contract_management_system.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest loginRequest)
    {
        User user = userRepository
                .findByUsername(loginRequest.getUsername())
                .orElse(null);

        if (user == null)
        {
            return ResponseEntity
                    .status(401)
                    .body("Invalid username or password");
        }

        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()))
        {
            return ResponseEntity
                    .status(401)
                    .body("Invalid username or password");
        }

        return ResponseEntity.ok(
                "Login successful! Role: " + user.getRole().getName());
    }
}