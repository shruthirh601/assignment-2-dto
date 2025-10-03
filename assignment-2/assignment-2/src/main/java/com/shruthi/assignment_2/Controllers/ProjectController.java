package com.shruthi.assignment_2.Controllers;


import com.shruthi.assignment_2.DTOs.ProjectDTO;
import com.shruthi.assignment_2.Model.Project;
import com.shruthi.assignment_2.Services.ProjectServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectController {

    @Autowired
    public ProjectServices projectService;
    public ProjectController(ProjectServices projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id){
        ProjectDTO pro = projectService.getProjectById(id);
        return (pro!=null)? new  ResponseEntity<>(pro,HttpStatus.OK):new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/project")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody @Valid ProjectDTO project){
        ProjectDTO pro = projectService.createProject(project);
        return (pro!=null)? new  ResponseEntity<>(pro,HttpStatus.OK):new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
