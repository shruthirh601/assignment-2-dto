package com.shruthi.assignment_2.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperDTO {
    private Long id;
    @NotBlank(message = "Developer name is required.")
    private String name;
    @NotNull(message = "Project IDs list is required.")
    @NotEmpty(message = "Developer must be assigned to at least one project.")
    private List<Long> projectIds;

    private Integer overdueTaskCount;
}

