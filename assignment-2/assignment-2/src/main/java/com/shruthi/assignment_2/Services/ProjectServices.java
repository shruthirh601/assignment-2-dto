package com.shruthi.assignment_2.Services;

import com.shruthi.assignment_2.DTOs.ProjectDTO;
import com.shruthi.assignment_2.DTOs.TaskDTO;
import com.shruthi.assignment_2.Model.Project;
import com.shruthi.assignment_2.Model.Task;
import com.shruthi.assignment_2.Repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServices {

    @Autowired
    public ProjectRepository projectRepository;
    public ProjectServices(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectDTO getProjectById(Long id) {
        Project pro= projectRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Project with id %s not found", id)));
        return convertProjectToDTO(pro);
    }

    public ProjectDTO createProject(ProjectDTO project) {
        Project pro = Project.builder().name(project.getName()).description(project.getDescription()).build();
        Project pro1 = projectRepository.save(pro);
        return convertProjectToDTO(pro);
    }

    public ProjectDTO convertProjectToDTO(Project saved) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(saved.getId());
        projectDTO.setName(saved.getName());
        projectDTO.setDescription(saved.getDescription());
        return projectDTO;
    }
}
