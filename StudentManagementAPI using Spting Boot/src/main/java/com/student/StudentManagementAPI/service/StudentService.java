package com.student.StudentManagementAPI.service;

import com.student.StudentManagementAPI.entity.Student;
import com.student.StudentManagementAPI.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService
{
    @Autowired
    private StudentRepository studentRepository;

    // Add Student
    public Student addStudent(Student student)
    {
        return studentRepository.save(student);
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

    // Update Student
    public Student updateStudent(Student student)
    {
        return studentRepository.save(student);
    }

    // Delete Student
    public String deleteStudent(Integer id)
    {
        studentRepository.deleteById(id);
        return "Student Deleted Successfully";
    }
}
