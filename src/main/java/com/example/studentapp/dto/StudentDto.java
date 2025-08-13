package com.example.studentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    private Long id;
    private String firstName;  // ✅ camelCase
    private String lastName;   // ✅ camelCase
    private int age;
    private String email;
}
