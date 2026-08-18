package com.student.StudentManagementAPI.service;

import com.student.StudentManagementAPI.entity.User;
import com.student.StudentManagementAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.student.StudentManagementAPI.repository.RoleRepository;
import com.student.StudentManagementAPI.entity.Role;
import com.student.StudentManagementAPI.dto.LoginResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import com.student.StudentManagementAPI.dto.ForgotPasswordRequest;
import com.student.StudentManagementAPI.dto.VerifyOtpRequest;
import com.student.StudentManagementAPI.dto.ResetPasswordRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@Service
public class AuthService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuditLogService auditLogService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String register(User user, HttpServletRequest request)
    {
        System.out.println("Username = " + user.getUsername());
        System.out.println("Password = " + user.getPassword());

        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null)
        {
            return "Username already exists";
        }

        if(user.getRole() == null)
        {
            Role role = roleRepository.findByRoleName("USER")
                    .orElseThrow(() -> new RuntimeException("USER role not found"));

            user.setRole(role);
        }

        if(user.getStatus() == null || user.getStatus().isEmpty())
        {
            user.setStatus("Active");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setCreatedBy(user.getRole().getId().longValue());

        userRepository.save(user);

        auditLogService.log(
                user.getUsername(),
                "REGISTRATION",
                "USER",
                user.getId(),
                "User registered successfully",
                request
        );

        return "Registration Successfull";
    }

    public LoginResponse login(User user, HttpServletRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        User dbUser = userRepository.findByUsername(user.getUsername());

        System.out.println("Username = " + dbUser.getUsername());
        System.out.println("Role = " + dbUser.getRole().getRoleName());

        auditLogService.log(
                dbUser.getUsername(),
                "LOGIN",
                "USER",
                dbUser.getId(),
                "User logged in successfully",
                request
        );

        String token = jwtService.generateToken(user.getUsername());

        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        return new LoginResponse(
                token,
                refreshToken,
                dbUser.getRole().getRoleName()
        );
    }

    public String logout(String username, HttpServletRequest request)
    {
        auditLogService.log(
                username,
                "LOGOUT",
                "USER",
                null,
                "User logged out successfully",
                request
        );

        return "Logout Successfull";
    }

    // Get All Users
    public java.util.List<User>getAllUsers()
    {
        return userRepository.findByIsDeleted("false");
    }

    // Search User by Username
    public java.util.List<User>searchUsers(String username)
    {
        return userRepository.findByUsernameContainingIgnoreCaseAndIsDeleted(username, "false");
    }

    // Update User
    public String updateUser(Long id, User user)
    {
        User existingUser = userRepository.findById(id).orElse(null);

        if(existingUser == null)
        {
            return "User Not Found";
        }

        existingUser.setUsername(user.getUsername());

        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

        existingUser.setEmail(user.getEmail());

        existingUser.setContact(user.getContact());

        existingUser.setAddress(user.getAddress());

        if(user.getProfilePhoto() != null)
        {
            existingUser.setProfilePhoto(user.getProfilePhoto());
        }

        userRepository.save(existingUser);

        return "User Updated Successfully";
    }

    // Update Own Profile
    public String updateProfile(String username, User user)
    {
        User existingUser = userRepository.findByUsername(username);

        if(existingUser == null)
        {
            return "User Not Found";
        }

        existingUser.setEmail(user.getEmail());
        existingUser.setContact(user.getContact());
        existingUser.setAddress(user.getAddress());

        userRepository.save(existingUser);

        return "Profile Updated Successfully";
    }

    // Upload Profile Photo
    public String uploadPhoto(Long id, MultipartFile file)
    {
        try
        {
            User existingUser = userRepository.findById(id).orElse(null);

            if(existingUser == null)
            {
                return "User Not Found";
            }

            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath))
            {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            existingUser.setProfilePhoto(fileName);

            userRepository.save(existingUser);

            return "Profile Photo Uploaded Successfully";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Photo Upload Failed";
        }
    }

    // Delete User
    public String deleteUser(Long id)
    {
        User user = userRepository.findById(id).orElse(null);

        if(user == null)
        {
            return "User Not Found";
        }

        user.setIsDeleted("true");
        user.setStatus("Inactive");

        userRepository.save(user);

        return "User Deleted Successfully";
    }

    public List<User>searchUser(String username)
    {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    public List<User>filterByStatus(String status)
    {
        return userRepository.findByStatusAndIsDeleted(status, "false");
    }

    public String forgotPassword(ForgotPasswordRequest request)
    {
        User user = userRepository.findByEmail(request.getEmail());

        if(user == null)
        {
            return "Email Not Registered";
        }

        String otp = otpService.generateOTP(request.getEmail());

        System.out.println("OTP = " + otp);

        emailService.sendOTP(request.getEmail(), otp);

        return "OTP Sent Successfully";
    }

    public String verifyOTP(VerifyOtpRequest request)
    {
        boolean valid = otpService.verifyOTP(
                request.getEmail(),
                request.getOtp()
        );

        if(valid)
        {
            return "OTP Verified Successfully";
        }

        return "Invalid OTP";
    }

    public String resetPassword(ResetPasswordRequest request,HttpServletRequest httpRequest)
    {
        if(!request.getNewPassword().equals(request.getConfirmPassword()))
        {
            return "New Password and Confirm Password do not match";
        }

        User user = userRepository.findByEmail(request.getEmail());

        if(user == null)
        {
            return "User Not Found";
        }

        if(!otpService.verifyOTP(request.getEmail(), request.getOtp()))
        {
            return "Invalid OTP";
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        auditLogService.log(
                user.getUsername(),
                "PASSWORD_CHANGE",
                "USER",
                user.getId(),
                "User password reset successfully",
                httpRequest
        );

        otpService.removeOTP(request.getEmail());

        return "Password Reset Successfully";
    }

    public String changePassword(String username, String currentPassword,
                                 String newPassword, String confirmPassword, HttpServletRequest request)
    {
        User existingUser = userRepository.findByUsername(username);

        if(existingUser == null)
        {
            return "User Not Found";
        }

        if(!passwordEncoder.matches(currentPassword, existingUser.getPassword()))
        {
            return "Current Password is Incorrect";
        }

        if(!newPassword.equals(confirmPassword))
        {
            return "New Password and Confirm Password do not match";
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(existingUser);

        auditLogService.log(
                existingUser.getUsername(),
                "PASSWORD_CHANGE",
                "USER",
                existingUser.getId(),
                "User password changed successfully",
                request
        );

        return "Password Changed Successfully";
    }
}
