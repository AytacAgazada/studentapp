package com.example.studentapp.serviceTest;
import com.example.studentapp.StudentappApplication;
import com.example.studentapp.dto.StudentDto;
import com.example.studentapp.entity.Student;
import com.example.studentapp.repository.StudentRepo;
import com.example.studentapp.service.StudentServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = StudentappApplication.class)
public class StudentIntegrationTest {


    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private StudentRepo repo;

    private Student testStudent;
    private Student testStudent2;


    @BeforeEach
    void setUp() {
        repo.deleteAll();


        testStudent = new Student(null,"Murad","Alizade",23,"m@mail.com");
        testStudent2 = new Student(null,"Ali","Aliyev",43,"a@mail.com");


        testStudent = repo.save(testStudent);
        testStudent2 = repo.save(testStudent2);
    }


    @AfterEach
    void cleanUp() {
        repo.deleteAll();
    }



    @Test
    void shouldCreateStudent() {
        StudentDto newStudent = new StudentDto(null,"Memmed","Memmedov",33,"ma@mail.com");

        StudentDto createdStudent = studentService.createStudent(newStudent);

        Assertions.assertThat(createdStudent.getId()).isNotNull();
        Assertions.assertThat(createdStudent.getFirstname()).isEqualTo("Memmed");
        Assertions.assertThat(createdStudent.getLastname()).isEqualTo("Memmedov");
        Assertions.assertThat(createdStudent.getEmail()).isEqualTo("ma@mail.com");

        Optional<Student> db = repo.findById(createdStudent.getId());

        Assertions.assertThat(db).isPresent();
        Assertions.assertThat(db.get().getFirstname()).isEqualTo("Memmed");


    }



    @Test
    void shouldGetAllStudents() {

        List<StudentDto> result = studentService.getAllStudents();

        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result).extracting(student -> student.getFirstname())
                .containsExactly(testStudent.getFirstname(),testStudent2.getFirstname());
    }



    @Test
    void shouldGetStudentById() {

        StudentDto result = studentService.getStudentById(testStudent.getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFirstname()).isEqualTo(testStudent.getFirstname());
        Assertions.assertThat(result.getLastname()).isEqualTo(testStudent.getLastname());
        Assertions.assertThat(result.getAge()).isEqualTo(testStudent.getAge());
        Assertions.assertThat(result.getEmail()).isEqualTo(testStudent.getEmail());

    }

    @Test
    void getStudentById_NotFound() {

        Assertions.assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(Student.class)
                .hasMessage("Student not found");

    }




}