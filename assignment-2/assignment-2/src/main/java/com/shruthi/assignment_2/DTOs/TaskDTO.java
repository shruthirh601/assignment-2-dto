package com.shruthi.assignment_2.DTOs;

import com.shruthi.assignment_2.Enums.TaskPriority;
import com.shruthi.assignment_2.Enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent; // Use FutureOrPresent to allow today's date
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id;

    @NotBlank(message = "Task name is required.")
    private String name;

    @NotBlank(message = "Task description is required.")
    private String description;

    @NotNull(message = "Task status is required.")
    private TaskStatus status;

    @NotNull(message = "Task priority is required.")
    private TaskPriority priority;

    // Due date is required, and must be today or in the future
    @NotNull(message = "Due date is required.")
    @FutureOrPresent(message = "Due date must be today or in the future.")
    private LocalDate dueDate;

    // Task must be assigned to a developer
    @NotNull(message = "Developer ID is required for task assignment.")
    private Long developerId;
}