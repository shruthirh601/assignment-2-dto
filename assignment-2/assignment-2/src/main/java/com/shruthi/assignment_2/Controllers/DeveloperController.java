package com.shruthi.assignment_2.Controllers;

import com.shruthi.assignment_2.DTOs.DeveloperDTO;
import com.shruthi.assignment_2.Model.Developer;
import com.shruthi.assignment_2.Services.DeveloperServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class DeveloperController {

    private final DeveloperServices developerServices;

    @Autowired
    public DeveloperController(DeveloperServices developerServices) {
        this.developerServices = developerServices;
    }

    @PostMapping("/dev")
    public ResponseEntity<DeveloperDTO> createDeveloper(@RequestBody @Valid DeveloperDTO developer){
        DeveloperDTO dev = developerServices.createDeveloper(developer);
        return new ResponseEntity<>(dev, HttpStatus.CREATED);
    }

    @GetMapping("/dev/{id}")
    public List<DeveloperDTO> getDevelopersByProjectId(@PathVariable Long id){
        return developerServices.getDevelopersByProjectId(id);
    }

    @GetMapping("/devs/overdue")
    public List<DeveloperDTO> getOverdueDevelopers(){
        return developerServices.getTop3DevelopersWithMostOverdueTasks();
    }
}