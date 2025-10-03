package com.shruthi.assignment_2.Services;

import com.shruthi.assignment_2.DTOs.TaskDTO;
import com.shruthi.assignment_2.Enums.TaskStatus;
import com.shruthi.assignment_2.Exceptions.MaxInProgressTasksException;
import com.shruthi.assignment_2.Exceptions.TaskAlreadyCompletedException;
import com.shruthi.assignment_2.Model.Developer;
import com.shruthi.assignment_2.Model.Task;
import com.shruthi.assignment_2.Repositories.DeveloperRepository;
import com.shruthi.assignment_2.Repositories.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServices {

    @Autowired
    public TaskRepository taskRepository;
    public DeveloperRepository developerRepository;
    public TaskServices(TaskRepository taskRepository, DeveloperRepository developerRepository) {
        this.taskRepository = taskRepository;
        this.developerRepository = developerRepository;
    }


    public TaskDTO createTask(@Valid TaskDTO newTask) {
        Developer developer = developerRepository.findById(newTask.getDeveloperId())
                .orElseThrow(() -> new RuntimeException("Developer not found")); // Use custom exception

        // IMPROVEMENT: Use the efficient repository query
        int inProgressCount = developerRepository.countInProgressTasks(developer.getId());

        // Check constraint before creating the task
        if (newTask.getStatus() == TaskStatus.IN_PROGRESS && inProgressCount >= 5) {
            throw new MaxInProgressTasksException("Developer already has 5 IN_PROGRESS tasks");
        }

        Task task = Task.builder()
                .name(newTask.getName())
                .description(newTask.getDescription())
                .status(newTask.getStatus())
                .priority(newTask.getPriority())
                .dueDate(newTask.getDueDate())
                .developer(developer)
                .build();

        Task saved = taskRepository.save(task);

        return convertTaskToDTO(saved);
    }

    public TaskDTO getTaskById(Long id) {
        Task saved = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return convertTaskToDTO(saved);
    }

    public TaskDTO updateTaskStatus(Long id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found")); // Use custom exception


        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new TaskAlreadyCompletedException("Cannot update a COMPLETED task");
        }


        if (newStatus == TaskStatus.IN_PROGRESS && task.getStatus() != TaskStatus.IN_PROGRESS) {
            // IMPROVEMENT: Use efficient repository query for counting
            Long developerId = task.getDeveloper().getId();
            int inProgressCount = developerRepository.countInProgressTasks(developerId);

            if (inProgressCount >= 5) {
                throw new MaxInProgressTasksException("Developer already has 5 IN_PROGRESS tasks");
            }
        }

        task.setStatus(newStatus);
        Task savedTask = taskRepository.save(task);

        return convertTaskToDTO(savedTask);
    }

    public List<TaskDTO> getTasksOverdue() {
        List<Task> overdueTasks = taskRepository.findOverdueTasks(LocalDate.now());

        return overdueTasks.stream()
                .map(this::convertTaskToDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO convertTaskToDTO(Task saved) {
        TaskDTO t = new TaskDTO();
        t.setId(saved.getId());
        t.setName(saved.getName());
        t.setDescription(saved.getDescription());
        t.setDueDate(saved.getDueDate());
        t.setPriority(saved.getPriority());
        t.setStatus(saved.getStatus());
        if (saved.getDeveloper() != null) {
            t.setDeveloperId(saved.getDeveloper().getId());
        }
        return t;
    }
}
