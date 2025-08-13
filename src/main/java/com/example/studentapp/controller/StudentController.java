package com.example.studentapp.controller;

import com.example.studentapp.dto.StudentDto;
import com.example.studentapp.service.StudentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentDto createStudent(@RequestBody StudentDto studentDto){
        return studentService.createStudent(studentDto);
    }

    @GetMapping("/{id}")
    public StudentDto getStudentById(@PathVariable Long id){
        return studentService.getStudentById(id);
    }

    @GetMapping
    public Iterable<StudentDto> getAllStudents(){
        return studentService.getAllStudents();
    }

    @PutMapping("/{id}")
    public StudentDto updateStudent(@PathVariable Long id,@RequestBody StudentDto studentDto){
        return studentService.updateStudent(id,studentDto);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
    }
}
