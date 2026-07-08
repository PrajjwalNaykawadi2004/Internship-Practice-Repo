package com.student.StudentManagementAPI.service;

import com.student.StudentManagementAPI.entity.User;
import com.student.StudentManagementAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService
{
    @Autowired
    private UserRepository userRepository;

    public String register(User user)
    {
        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null)
        {
            return "Username already exists";
        }

        userRepository.save(user);
        return "Registration Successful";
    }

    public String login(User user)
    {
        User existingUser = userRepository.findByUsername(user.getUsername());

        if(existingUser == null)
        {
            return "User not found";
        }

        if(existingUser.getPassword().equals(user.getPassword()))
        {
            return "Login Successfull";
        }

        return "Invalid Password";
    }

    public String logout()
    {
        return "Logout Successful";
    }
}
