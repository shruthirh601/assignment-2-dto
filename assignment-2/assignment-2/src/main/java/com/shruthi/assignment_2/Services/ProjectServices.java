package com.shruthi.assignment_2.Services;

import com.shruthi.assignment_2.DTOs.ProjectDTO;
import com.shruthi.assignment_2.Model.Developer;
import com.shruthi.assignment_2.Model.Project;
import com.shruthi.assignment_2.Repositories.DeveloperRepository;
import com.shruthi.assignment_2.Repositories.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServices {

    private final ProjectRepository projectRepository;
    private final DeveloperRepository developerRepository;

    @Autowired
    public ProjectServices(ProjectRepository projectRepository, DeveloperRepository developerRepository) {
        this.projectRepository = projectRepository;
        this.developerRepository = developerRepository;
    }

    public ProjectDTO getProjectById(Long id) {
        Project pro = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Project with id %s not found", id)));
        return convertProjectToDTO(pro);
    }

    public ProjectDTO createProject(ProjectDTO project) {
        Project pro = Project.builder()
                .name(project.getName())
                .description(project.getDescription())
                .build();
        Project savedProject = projectRepository.save(pro);
        return convertProjectToDTO(savedProject);
    }

    public ProjectDTO convertProjectToDTO(Project saved) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(saved.getId());
        projectDTO.setName(saved.getName());
        projectDTO.setDescription(saved.getDescription());

        if (saved.getDevelopers() != null) {
            Set<Long> developerIds = saved.getDevelopers().stream()
                    .map(Developer::getId)
                    .collect(Collectors.toSet());
            projectDTO.setDeveloperIds(developerIds);
        } else {
            projectDTO.setDeveloperIds(new HashSet<>());
        }
        return projectDTO;
    }
}