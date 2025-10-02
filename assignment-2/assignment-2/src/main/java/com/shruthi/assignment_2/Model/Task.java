package com.shruthi.assignment_2.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shruthi.assignment_2.Enums.TaskPriority;
import com.shruthi.assignment_2.Enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "developer_id")
    private Developer developer;

    @Future
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dueDate;

}
