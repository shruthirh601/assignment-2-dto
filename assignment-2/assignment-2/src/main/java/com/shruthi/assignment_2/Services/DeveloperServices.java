package com.shruthi.assignment_2.Services;

import com.shruthi.assignment_2.DTOs.DeveloperDTO;
import com.shruthi.assignment_2.Enums.TaskStatus;
import com.shruthi.assignment_2.Model.Developer;
import com.shruthi.assignment_2.Model.Project;
import com.shruthi.assignment_2.Model.Task;
import com.shruthi.assignment_2.Repositories.DeveloperRepository;
import com.shruthi.assignment_2.Repositories.ProjectRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeveloperServices {
    @Autowired
    public DeveloperRepository developerRepository;
    @Autowired
    public ProjectRepository projectRepository;
    @Autowired
    public ProjectServices projectServices;
    public DeveloperServices(DeveloperRepository developerRepository,  ProjectRepository projectRepository,ProjectServices projectServices){
        this.developerRepository = developerRepository;
        this.projectRepository = projectRepository;
        this.projectServices = projectServices;
    }

    @Transactional
    public DeveloperDTO createDeveloper(@RequestBody @Valid DeveloperDTO developer) {
        Developer dev = new Developer();
        dev.setName(developer.getName());

        if (developer.getProjectIds() == null) {
            throw new NullPointerException("Project IDs are null");
        }

        Set<Project> projects = new HashSet<>();
        for (Long projectId : developer.getProjectIds()) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

            projects.add(project);
            project.getDevelopers().add(dev);
        }

        dev.setProjects(projects);

        Developer saved = developerRepository.save(dev);

        DeveloperDTO dto = new DeveloperDTO();
        dto.setId(saved.getId());
        dto.setName(saved.getName());
        dto.setProjectIds(
                saved.getProjects().stream().map(Project::getId).collect(Collectors.toList())
        );

        return dto;
    }


    @Transactional
    public List<DeveloperDTO> getDevelopersByProjectId(@PathVariable Long id){
            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Project not found"));

            Set<Developer> developers = new HashSet<>(project.getDevelopers());
                 return developers.stream()
                .map(dev -> {
                    DeveloperDTO dto = new DeveloperDTO();
                    dto.setId(dev.getId());
                    dto.setName(dev.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<DeveloperDTO> getTop3DevelopersWithMostOverdueTasks() {
        List<Developer> allDevelopers = developerRepository.findAll();

        return allDevelopers.stream()
                .map(dev -> {

                    List<Task> tasksCopy = new ArrayList<>(dev.getTasks());

                    long count = tasksCopy.stream()
                            .filter(task -> task.getDueDate().isBefore(LocalDate.now()) &&
                                    task.getStatus() != TaskStatus.COMPLETED)
                            .count();

                    DeveloperDTO dto = new DeveloperDTO();
                    dto.setId(dev.getId());
                    dto.setName(dev.getName());
                    dto.setOverdueTaskCount((int) count);
                    return dto;
                })
                .sorted(Comparator.comparingInt(DeveloperDTO::getOverdueTaskCount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }
}
