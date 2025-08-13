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

    @KafkaListener(topics = "${kafka.topic.student-updated}",groupId = "student-updated-group")
    public void lislistenStudentCreated(StudentDto studentDto){
        log.info("Received student updated event: {}",studentDto);

        String subject = "Welcome to Student App";
        String body = "Helllo "+studentDto.getFirstName();
        emailService.sendEmail(studentDto.getEmail(),subject,body);
        log.info("Email sent to {}",studentDto.getEmail());
    }

    @KafkaListener(topics = "student-created-topic", groupId = "student-group1")
    public void listenStudentCreated(ConsumerRecord<String, StudentDto> record) {
        int partition = record.partition();
        long offset = record.offset();

        log.info("Group-1 Received record Kafka for partition {} offset {}", partition, offset);

        String subject = "Informational message";
        String body = " Sincerely " + record.value().getFirstName() + " " + record.value().getLastName() + " your registration is complete ";

        emailService.sendEmail(record.value().getEmail(), subject, body);
        log.info("Group-1 Email sent successfully {}", record.value().getEmail());

    }

    @KafkaListener(topics = "${kafka.topic.student-created}", groupId = "student-created-group-2")
    public void listenStudentCreatedGroup2(ConsumerRecord<String, StudentDto> record) {
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
