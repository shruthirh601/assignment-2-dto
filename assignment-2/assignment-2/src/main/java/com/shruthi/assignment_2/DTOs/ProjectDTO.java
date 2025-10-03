package com.shruthi.assignment_2.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    private Long id;
    @NotBlank(message = "Project name is required.")
    private String name;
    @NotBlank(message = "Project description is required.")
    private String description;
    private Set<Long> developerIds;
}