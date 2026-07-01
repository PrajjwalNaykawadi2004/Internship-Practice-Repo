package com.student.StudentManagementAPI.controller;

import com.student.StudentManagementAPI.entity.Student;
import com.student.StudentManagementAPI.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController
{
    @Autowired
    private StudentService studentService;

    // Add Student
    @PostMapping
    public Student addStudent(@RequestBody Student student)
    {
        return studentService.addStudent(student);
    }

    // Get All Students
    @GetMapping
    public List<Student> getAllStudent()
    {
        return studentService.getAllStudents();
    }

    // Get Student By id
    @GetMapping("/{id}")
    public Optional<Student> getStudentById(@PathVariable Integer id)
    {
        return studentService.getStudentById(id);
    }

    // Update Student
    @PutMapping
    public Student updateStudent(@RequestBody Student student)
    {
        return studentService.updateStudent(student);
    }

    // Delete Student
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id)
    {
        return studentService.deleteStudent(id);
    }
}
