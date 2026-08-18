package com.student.StudentManagementAPI.service;

import com.student.StudentManagementAPI.entity.Student;
import com.student.StudentManagementAPI.repository.RoleRepository;
import com.student.StudentManagementAPI.repository.StudentRepository;
import com.student.StudentManagementAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import jakarta.servlet.http.HttpServletRequest;
import com.student.StudentManagementAPI.entity.User;
import com.student.StudentManagementAPI.entity.Role;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService
{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Add Student
    public Student addStudent(Student student,
                              String username,
                              HttpServletRequest request)
    {
        Student savedStudent = studentRepository.save(student);

        // Create User
        User user = new User();
        user.setUsername(savedStudent.getName());
        user.setEmail(savedStudent.getEmail());
        user.setPassword(passwordEncoder.encode("123456")); // Default Password
        user.setStatus("Active");
        user.setContact(savedStudent.getContact());
        user.setAddress(savedStudent.getAddress());
        user.setDepartment(savedStudent.getDepartment());
        user.setCourse(savedStudent.getCourse());
        user.setAge(savedStudent.getAge());

        Role role = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("USER Role Not Found"));

        user.setRole(role);

        userRepository.save(user);

        auditLogService.log(
                username,
                "CREATE",
                "STUDENT",
                savedStudent.getId().longValue(),
                "Student created successfully",
                request
        );

        return savedStudent;
    }

    // Get All Students
    public List<Student> getAllStudents()
    {
        return studentRepository.findAll();
    }

    // Get Student By ID
    public Optional<Student> getStudentById(Integer id)
    {
        return studentRepository.findById(id);
    }

    public Student updateStudent(Integer id,
                                 Student student,
                                 String username,
                                 HttpServletRequest request)
    {
        Student existingStudent = studentRepository.findById(id).orElse(null);

        if(existingStudent != null)
        {
            existingStudent.setName(student.getName());
            existingStudent.setEmail(student.getEmail());
            existingStudent.setDepartment(student.getDepartment());
            existingStudent.setCourse(student.getCourse());
            existingStudent.setCity(student.getCity());
            existingStudent.setAge(student.getAge());
            existingStudent.setContact(student.getContact());
            existingStudent.setAddress(student.getAddress());

            Student updatedStudent = studentRepository.save(existingStudent);

            auditLogService.log(
                    username,
                    "UPDATE",
                    "STUDENT",
                    updatedStudent.getId().longValue(),
                    "Student updated successfully",
                    request
            );

            return updatedStudent;
        }

        return null;
    }

    // Delete Student
    public String deleteStudent(Integer id,
                                String username,
                                HttpServletRequest request)
    {
        Student existingStudent = studentRepository.findById(id).orElse(null);

        if(existingStudent == null)
        {
            return "Student Not Found";
        }

        studentRepository.deleteById(id);

        auditLogService.log(
                username,
                "DELETE",
                "STUDENT",
                id.longValue(),
                "Student deleted successfully",
                request
        );

        return "Student Deleted Successfully";
    }

    public List<Student>searchByEmail(String email)
    {
        return studentRepository.findByEmail(email);
    }

    public List<Student>searchByDepartment(String department)
    {
        return studentRepository.findByDepartment(department);
    }

    public List<Student>searchByCity(String city)
    {
        return studentRepository.findByCity(city);
    }

    public Page<Student>getStudentWithPagination(int pageNo, int pageSize)
    {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return studentRepository.findAll(pageable);
    }

    public List<Student>getStudentSortAsc(String field)
    {
        return studentRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    public List<Student>getStudentDesc(String field)
    {
        return studentRepository.findAll(Sort.by(Sort.Direction.DESC, field));
    }

    // Total Students Count
    public long getStudentCount()
    {
        return studentRepository.count();
    }
}
