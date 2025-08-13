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

    @KafkaListener(topics = "${kafka.topic.student-created}", groupId = "student-created-group")
    public void listenStudentCreated(ConsumerRecord<String, StudentDto> record) {
        log.info("Received student created event: {}",record.value());

        int partition = record.partition();
        long offset = record.offset();

        log.info("Partition: {}, Offset: {}",partition,offset);
        String subject = "Welcome to Student App";
        String body = "Helllo "+record.value().getFirstName();
        emailService.sendEmail(record.value().getEmail(),subject,body);
        log.info("Email sent to {}",record.value().getEmail());
    }

    @KafkaListener(topics = "${kafka.topic.student-deleted}",groupId = "student-deleted-group")
    public void listenStudentDeleted(StudentDto studentDto){
        log.info("Received student deleted event: {}",studentDto);

        String subject = "Student Deleted";
        String body = "Helllo "+studentDto.getFirstName()+" your account is deleted";

        emailService.sendEmail(studentDto.getEmail(),subject,body);
        log.info("Email sent to {}",studentDto.getEmail());

    }
}
