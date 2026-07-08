package com.student.StudentManagementAPI.controller;

import com.student.StudentManagementAPI.entity.User;
import com.student.StudentManagementAPI.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController
{
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody User user)
    {
        return authService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user)
    {
        return authService.login(user);
    }

    @PostMapping("/logout")
    public String logout()
    {
        return authService.logout();
    }
}
