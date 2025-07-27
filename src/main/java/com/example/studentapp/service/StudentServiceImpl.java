package com.example.studentapp.service;

import com.example.studentapp.dto.StudentDto;
import com.example.studentapp.entity.Student;
import com.example.studentapp.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final StudentRepo studentRepo;

    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        Student student =new Student();
        student.setFirstname(studentDto.getFirstname());
        student.setLastname(studentDto.getLastname());
        student.setAge(studentDto.getAge());
        student.setEmail(studentDto.getEmail());
        Student savedStudent = studentRepo.save(student);
        return new StudentDto(savedStudent.getId(),savedStudent.getFirstname(),savedStudent.getLastname(),savedStudent.getAge(),savedStudent.getEmail());
    }

    @Override
    public StudentDto getStudentById(Long id) {
        Student student = studentRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Student not found"));
        return new StudentDto(student.getId(),student.getFirstname(),student.getLastname(),student.getAge(),student.getEmail());
    }

    @Override
    public List<StudentDto> getAllStudents() {
        return studentRepo.findAll().stream()
                .map(student -> new StudentDto(student.getId(),student.getFirstname(),student.getLastname(),student.getAge(),student.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto updateStudent(Long id,StudentDto studentDto) {
        Student exist = studentRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Student not found"));
        exist.setFirstname(studentDto.getFirstname());
        exist.setLastname(studentDto.getLastname());
        exist.setAge(studentDto.getAge());
        exist.setEmail(studentDto.getEmail());
        Student updatedStudent = studentRepo.save(exist);
        return new StudentDto(updatedStudent.getId(),updatedStudent.getFirstname(),updatedStudent.getLastname(),updatedStudent.getAge(),updatedStudent.getEmail());
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepo.deleteById(id);
    }


}
