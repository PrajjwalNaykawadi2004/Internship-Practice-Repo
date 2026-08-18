package com.student.StudentManagementAPI.service;

import com.student.StudentManagementAPI.entity.User;
import com.student.StudentManagementAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;


    // ================= GET ALL USERS =================

    public List<User> getAllUsers()
    {
        return userRepository.findByIsDeleted("false");
    }


    // ================= UPDATE USER =================

    public User updateUser(Long id, User user)
    {
        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser != null)
        {
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setStatus(user.getStatus());

            if (existingUser.getRole() != null) {
                existingUser.setUpdatedBy(
                        existingUser.getRole().getRoleName()
                );
            }

            return userRepository.save(existingUser);
        }

        return null;
    }


    // ================= DELETE USER =================

    public String deleteUser(Long id)
    {
        User existingUser = userRepository.findById(id).orElse(null);

        if(existingUser != null)
        {
            // Soft Delete
            existingUser.setIsDeleted("true");
            existingUser.setStatus("Inactive");

            if (existingUser.getRole() != null) {
                existingUser.setUpdatedBy(
                        existingUser.getRole().getRoleName()
                );
            }

            userRepository.save(existingUser);


            // ================= ACTIVITY LOG =================

            auditLogService.log(
                    existingUser.getUsername(),
                    "DELETE",
                    "USER",
                    existingUser.getId(),
                    "User deleted successfully",
                    null
            );


            return "User Deleted Successfully";
        }

        return "User Not Found";
    }


    // ================= PAGINATION =================

    public Page<User> getUsersWithPagination(int page, int size)
    {
        return userRepository.findAll(
                PageRequest.of(page, size)
        );
    }


    // ================= SORT BY NAME =================

    public List<User> sortByUsername()
    {
        return userRepository.findAll(
                Sort.by("username")
        );
    }


    // ================= SORT BY DATE =================

    public List<User> sortByCreatedDate()
    {
        return userRepository.findAll(
                Sort.by("createdDate")
        );
    }


    // ================= SEARCH =================

    public List<User> searchByUsername(String username)
    {
        return userRepository.findByUsernameContainingIgnoreCase(
                username
        );
    }


    // ================= STATUS =================

    public List<User> getByStatus(String status)
    {
        return userRepository.findByStatusAndIsDeleted(
                status,
                "false"
        );
    }
}