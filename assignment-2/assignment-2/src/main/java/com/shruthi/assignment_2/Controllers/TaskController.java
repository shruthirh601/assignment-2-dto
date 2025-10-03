package com.shruthi.assignment_2.Controllers;

import com.shruthi.assignment_2.DTOs.TaskDTO;
import com.shruthi.assignment_2.Enums.TaskStatus;
import com.shruthi.assignment_2.Model.Task;
import com.shruthi.assignment_2.Services.TaskServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController {

    @Autowired(required = true)
    public TaskServices taskServices;
    public TaskController(TaskServices taskServices) {
        this.taskServices = taskServices;
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO newTask) {
        TaskDTO t = taskServices.createTask(newTask);
        return (t!=null)? new ResponseEntity<>(newTask, HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id){
        TaskDTO t = taskServices.getTaskById(id);
        return (t!=null)? new ResponseEntity<>(t, HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable Long id,@RequestParam String newStatus) {
        TaskStatus statusEnum;
        try {
            statusEnum = TaskStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

        TaskDTO updatedTask = taskServices.updateTaskStatus(id, statusEnum);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/tasks/overdue")
    public List<TaskDTO> getOverdueTasks() {
        return taskServices.getTasksOverdue();
    }
}
