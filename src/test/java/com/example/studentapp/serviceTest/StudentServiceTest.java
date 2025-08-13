package com.example.studentapp.serviceTest;

import com.example.studentapp.dto.StudentDto;
import com.example.studentapp.entity.Student;
import com.example.studentapp.repository.StudentRepo;
import com.example.studentapp.service.EmailService;
import com.example.studentapp.service.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepo studentRepo;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    public void testCreateStudent(){
        StudentDto inputDto = new StudentDto(null,"Orxan","Alizade",24,"a@mail.com");

        Student savedStudent = new Student();
        savedStudent.setFirstName(inputDto.getFirstName());
        savedStudent.setLastName(inputDto.getLastName());
        savedStudent.setAge(inputDto.getAge());
        savedStudent.setEmail(inputDto.getEmail());

        when(studentRepo.save(any(Student.class))).thenReturn(savedStudent);

        StudentDto outputDto = studentService.createStudent(inputDto);

        assertNotNull(outputDto);
        assertEquals(savedStudent.getFirstName(),outputDto.getFirstName());
        assertEquals(savedStudent.getLastName(),outputDto.getLastName());
        assertEquals(savedStudent.getAge(),outputDto.getAge());
        assertEquals(savedStudent.getEmail(),outputDto.getEmail());

        verify(emailService,times(1)).sendEmail(eq("a@mail.com"),anyString(),anyString());
    }

    @Test
    public void testGetStudentById(){
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Orxan");
        student.setLastName("Alizade");
        student.setAge(24);
        student.setEmail("<a@mail.com>");

        when(studentRepo.findById(anyLong())).thenReturn(Optional.of(student));

        StudentDto outputDto = studentService.getStudentById(1L);

        assertNotNull(outputDto);
        assertEquals(student.getId(),outputDto.getId());
        assertEquals(student.getFirstName(),outputDto.getFirstName());
        assertEquals(student.getLastName(),outputDto.getLastName());
        assertEquals(student.getAge(),outputDto.getAge());
        assertEquals(student.getEmail(),outputDto.getEmail());

    }

    @Test
    public void testGetStudens(){
        Student student1 = new Student(1L, "Orxan", "Alizade", 24, "orxan@mail.com");
        Student student2 = new Student(2L, "Anar", "Karimov", 25, "anar@mail.com");

        when(studentRepo.findAll()).thenReturn(List.of(student1,student2));

        List<StudentDto> outputDtos = studentService.getAllStudents();

        assertEquals(1L,outputDtos.get(0).getId());
        assertEquals("Orxan", outputDtos.get(0).getFirstName());
        assertEquals("Alizade", outputDtos.get(0).getLastName());
        assertEquals(24, outputDtos.get(0).getAge());
        assertEquals("orxan@mail.com", outputDtos.get(0).getEmail());
    }

    @Test
    void getStudentById_NotFound_ThrowsException() {
        Long id = 2L;

        when(studentRepo.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,

                () -> studentService.getStudentById(id)
        );

        assertEquals("Student not found", exception.getMessage());
    }



    @Test
    void getAllStudents_Error() {

        when(studentRepo.findAll()).thenThrow(new RuntimeException("Database Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> {
                    studentService.getAllStudents();
                });

        assertEquals("Database Error", thrown.getMessage());

    }



    @Test
    void deleteStudent_WhenStudentDoesNotExist_ShouldThrowException() {

        Long studentId = 100L;
        when(studentRepo.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.deleteStudent(studentId));

        verify(studentRepo).findById(studentId);
        verify(studentRepo, never()).deleteById(anyLong());
        verify(emailService, never()).sendEmail(any(), any(), any());
    }

    @Test
    void testUpdateStudent(){
        Long id = 1L;
        Student existingStudent = new Student(id, "Murad", "Alizade", 20, "m@mail.com");

        StudentDto newData = new StudentDto(id, "Ali", "Memmedov", 22, "a@mail.com");

        Student updatedStudent = new Student(id, "Senan", "Veliyev", 22, "b@mail.com");

        when(studentRepo.findById(id)).thenReturn(Optional.of(existingStudent));
        when(studentRepo.save(any(Student.class))).thenReturn(updatedStudent);

        StudentDto result = studentService.updateStudent(id, newData);

        assertNotNull(result);
        assertEquals("Senan", result.getFirstName());
        assertEquals("Veliyev", result.getLastName());
        assertEquals(22, result.getAge());
        assertEquals("b@mail.com", result.getEmail());

        verify(studentRepo).findById(id);
        verify(studentRepo).save(existingStudent);
    }
}