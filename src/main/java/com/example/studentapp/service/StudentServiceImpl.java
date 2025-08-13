package com.example.studentapp.service;

import com.example.studentapp.dto.StudentDto;
import com.example.studentapp.entity.Student;
import com.example.studentapp.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final StudentRepo studentRepo;
    private final KafkaTemplate<String, StudentDto> kafkaTemplate;

    @Value("${kafka.topic.student-created}")
    private String studentCreatedTopic;

    @Value("${kafka.topic.student-deleted}")
    private String studentDeletedTopic;


    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setAge(studentDto.getAge());
        student.setEmail(studentDto.getEmail());
        String savedEmail = studentRepo.save(student).getEmail();

        kafkaTemplate.send(studentCreatedTopic,studentDto);
        log.info("Student created event sent to kafka topic {}",studentCreatedTopic);
        return new StudentDto(student.getId(),student.getFirstName(),student.getLastName(),student.getAge(),savedEmail);

    }

//    @Override
//
//
////    @Override
////    public StudentDto createStudent(StudentDto studentDto) {
////        Student student =new Student();
////        student.setFirstName(studentDto.getFirstName());
////        student.setLastName(studentDto.getLastName());
////        student.setAge(studentDto.getAge());
////        student.setEmail(studentDto.getEmail());
////        Student savedStudent = studentRepo.save(student);
////        String subject = "Welcome to Student App";
////        String body = "Helllooo 🌞 "+savedStudent.getFirstName();
////        emailService.sendEmail(savedStudent.getEmail(),subject,body);
////        return new StudentDto(savedStudent.getId(),savedStudent.getFirstName(),savedStudent.getLastName(),savedStudent.getAge(),savedStudent.getEmail());
////    }

    @Override
    public StudentDto getStudentById(Long id) {
        Student student = studentRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Student not found"));
        return new StudentDto(student.getId(),student.getFirstName(),student.getLastName(),student.getAge(),student.getEmail());
    }

    @Override
    public List<StudentDto> getAllStudents() {
        return studentRepo.findAll().stream()
                .map(student -> new StudentDto(student.getId(),student.getFirstName(),student.getLastName(),student.getAge(),student.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto updateStudent(Long id,StudentDto studentDto) {
        Student exist = studentRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Student not found"));
        exist.setFirstName(studentDto.getFirstName());
        exist.setLastName(studentDto.getLastName());
        exist.setAge(studentDto.getAge());
        exist.setEmail(studentDto.getEmail());
        Student updatedStudent = studentRepo.save(exist);
        return new StudentDto(updatedStudent.getId(),updatedStudent.getFirstName(),updatedStudent.getLastName(),updatedStudent.getAge(),updatedStudent.getEmail());
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepo.findById(id).orElseThrow(()->new RuntimeException("Student not found"));
        studentRepo.deleteById(id);
        log.warn("Student with id {} deleted",id);

        StudentDto studentDto = new StudentDto(student.getId(),student.getFirstName(),student.getLastName(),student.getAge(),student.getEmail());
        kafkaTemplate.send(studentDeletedTopic,studentDto);
        log.info("Student deleted event sent to kafka topic {}",id);
    }


}
