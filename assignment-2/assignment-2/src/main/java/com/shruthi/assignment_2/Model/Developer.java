package com.shruthi.assignment_2.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "developer")
@Builder
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "developer_project", joinColumns = @JoinColumn(name = "developer_id"),
    inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<Project> projects;

    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

}
