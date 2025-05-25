package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ChildDTO {

    private Long id; // Optional for updates

    @NotBlank(message = "Child name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Current education level is required")
    private String currentEducationLevel;


    private Long familyProfileId;
}
