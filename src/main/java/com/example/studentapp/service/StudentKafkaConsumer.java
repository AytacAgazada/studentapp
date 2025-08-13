package com.example.studentapp.service;

import com.example.studentapp.dto.StudentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentKafkaConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "${kafka.topic.student-created}", groupId = "student-group")
    public void lislistenStudentCreated(StudentDto studentDto){
        log.info("Received student updated event: {}", studentDto);

        // Email dəyərinin null olub-olmadığını yoxlayın
        if (studentDto != null && studentDto.getEmail() != null && !studentDto.getEmail().isEmpty()) {
            String subject = "Welcome to Student App";
            String body = "Helllo " + studentDto.getFirstName();
            emailService.sendEmail(studentDto.getEmail(), subject, body);
            log.info("Email sent to {}", studentDto.getEmail());
        } else {
            log.warn("Group 'student-group' üçün email göndərilmədi. Tələbənin email adresi yoxdur. Tələbənin məlumatları: {}", studentDto);
        }
    }

    @KafkaListener(topics = "${kafka.topic.student-created}", groupId = "student-group1")
    public void listenStudentCreated(ConsumerRecord<String, StudentDto> record) {
        StudentDto studentDto = record.value();

        // Email dəyərinin null olub-olmadığını yoxlayın
        if (studentDto != null && studentDto.getEmail() != null && !studentDto.getEmail().isEmpty()) {
            int partition = record.partition();
            long offset = record.offset();
            log.info("Group-1 Kafka rekord qəbul edildi. Partition: {} Offset: {}", partition, offset);

            String subject = "Informational message";
            String body = " Sincerely " + studentDto.getFirstName() + " " + studentDto.getLastName() + " your registration is complete ";
            emailService.sendEmail(studentDto.getEmail(), subject, body);
            log.info("Group-1 Email uğurla göndərildi {}", studentDto.getEmail());
        } else {
            log.warn("Group 'student-group1' üçün email göndərilmədi. Tələbənin email adresi yoxdur. Record: {}", record);
        }
    }

    @KafkaListener(topics = "${kafka.topic.student-created}", groupId = "student-created-group-2")
    public void listenStudentCreatedGroup2(ConsumerRecord<String, StudentDto> record) {
        StudentDto studentDto = record.value();

        // Email dəyərinin null olub-olmadığını yoxlayın
        if (studentDto != null && studentDto.getEmail() != null && !studentDto.getEmail().isEmpty()) {
            log.info("Received student created event: {}", studentDto);
            int partition = record.partition();
            long offset = record.offset();
            log.info("Partition: {}, Offset: {}", partition, offset);

            String subject = "Welcome to Student App";
            String body = "Helllo " + studentDto.getFirstName();
            emailService.sendEmail(studentDto.getEmail(), subject, body);
            log.info("Email sent to {}", studentDto.getEmail());
        } else {
            log.warn("Group 'student-created-group-2' üçün email göndərilmədi. Tələbənin email adresi yoxdur. Record: {}", record);
        }
    }

    @KafkaListener(topics = "${kafka.topic.student-deleted}", groupId = "student-deleted-group")
    public void listenStudentDeleted(StudentDto studentDto){
        log.info("Received student deleted event: {}", studentDto);

        // Email dəyərinin null olub-olmadığını yoxlayın
        if (studentDto != null && studentDto.getEmail() != null && !studentDto.getEmail().isEmpty()) {
            String subject = "Student Deleted";
            String body = "Helllo " + studentDto.getFirstName() + " your account is deleted";
            emailService.sendEmail(studentDto.getEmail(), subject, body);
            log.info("Email sent to {}", studentDto.getEmail());
        } else {
            log.warn("Group 'student-deleted-group' üçün email göndərilmədi. Tələbənin email adresi yoxdur. Tələbənin məlumatları: {}", studentDto);
        }
    }
}