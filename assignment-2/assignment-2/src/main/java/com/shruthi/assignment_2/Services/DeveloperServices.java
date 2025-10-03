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

    public DeveloperServices(DeveloperRepository developerRepository, ProjectRepository projectRepository, ProjectServices projectServices) {
        this.developerRepository = developerRepository;
        this.projectRepository = projectRepository;
        this.projectServices = projectServices;
    }

    @Transactional
    public DeveloperDTO createDeveloper(DeveloperDTO developer) {


        Developer dev = new Developer();
        dev.setName(developer.getName());

        Set<Project> projects = new HashSet<>();
        for (Long projectId : developer.getProjectIds()) {

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new RuntimeException("Project with ID " + projectId + " not found."));

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
    public List<DeveloperDTO> getDevelopersByProjectId(Long id){

        List<Developer> developers = developerRepository.findDevelopersByProjectId(id);


        if (developers.isEmpty() && !projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found: " + id); // Use a custom exception here
        }


        return developers.stream()
                .map(dev -> {
                    DeveloperDTO dto = new DeveloperDTO();
                    dto.setId(dev.getId());
                    dto.setName(dev.getName());

                    dto.setProjectIds(
                            dev.getProjects().stream().map(Project::getId).collect(Collectors.toList())
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public List<DeveloperDTO> getTop3DevelopersWithMostOverdueTasks() {

        List<Object[]> results = developerRepository.findTop3DevelopersWithMostOverdueTasks();

        return results.stream()
                .map(row -> {
                    DeveloperDTO dto = new DeveloperDTO();

                    dto.setId(((Number) row[0]).longValue());
                    dto.setName((String) row[1]);
                    dto.setOverdueTaskCount(((Number) row[2]).intValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }
//    // Inside DeveloperServices.java
//    @Transactional
//    public List<DeveloperDTO> getTop3DevelopersWithMostOverdueTasks() {
//        List<Developer> allDevelopers = developerRepository.findAll(); // Fetches Developers, but LAZILY loads Projects
//
//        return allDevelopers.stream()
//                .map(dev -> {
//                    long count = dev.getTasks().stream() // <-- 'count' is defined here
//                            .filter(task -> task.getDueDate().isBefore(LocalDate.now()) &&
//                                    task.getStatus() != TaskStatus.COMPLETED)
//                            .count();
//                    DeveloperDTO dto = new DeveloperDTO();
//                    dto.setId(dev.getId());
//                    dto.setName(dev.getName());
//                    dto.setProjectIds(
//                            dev.getProjects().stream().map(Project::getId).collect(Collectors.toList())
//                    );
//                    dto.setOverdueTaskCount((int) count);
//                    return dto;
//                })
//                // ... sorting and limiting ...
//                .collect(Collectors.toList());
//    }
}
