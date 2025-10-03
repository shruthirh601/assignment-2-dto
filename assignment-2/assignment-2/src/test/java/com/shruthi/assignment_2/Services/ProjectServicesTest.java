/*package com.shruthi.assignment_2.Services;

import com.shruthi.assignment_2.DTOs.ProjectDTO;
import com.shruthi.assignment_2.Model.Developer;
import com.shruthi.assignment_2.Model.Project;
import com.shruthi.assignment_2.Repositories.DeveloperRepository;
import com.shruthi.assignment_2.Repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServicesTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private ProjectServices projectServices;

    private Developer dev1;
    private Developer dev2;
    private ProjectDTO projectDTO;
    private Project projectEntity;

    @BeforeEach
    void setUp() {
        dev1 = Developer.builder().id(1L).name("Alice").projects(new HashSet<>()).build();
        dev2 = Developer.builder().id(2L).name("Bob").projects(new HashSet<>()).build();

        projectDTO = new ProjectDTO(null, "New App", "Building a mobile app", Set.of(1L, 2L));

        projectEntity = Project.builder()
                .id(10L)
                .name("New App")
                .description("Building a mobile app")
                .developers(new HashSet<>(Arrays.asList(dev1, dev2)))
                .build();
    }

    @Test
    void createProject_Success_LinksDevelopers() {

        when(developerRepository.findById(1L)).thenReturn(Optional.of(dev1));
        when(developerRepository.findById(2L)).thenReturn(Optional.of(dev2));


        when(projectRepository.save(any(Project.class)))
                .thenReturn(projectEntity);


        when(developerRepository.save(any(Developer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        ProjectDTO resultDTO = projectServices.createProject(projectDTO);

        assertNotNull(resultDTO, "The returned DTO should not be null.");
        assertEquals(10L, resultDTO.getId(), "The ID should be set by the mock save.");
        assertEquals(2, resultDTO.getDeveloperIds().size(), "Both developer IDs should be present.");
        assertTrue(resultDTO.getDeveloperIds().containsAll(Set.of(1L, 2L)), "Developer IDs 1 and 2 should be included.");

        verify(projectRepository, times(1)).save(any(Project.class));
        verify(developerRepository, times(1)).findById(1L);
        verify(developerRepository, times(1)).findById(2L);

        verify(developerRepository, times(2)).save(any(Developer.class));
    }

    @Test
    void createProject_DeveloperNotFound_ThrowsException() {

        ProjectDTO invalidDTO = new ProjectDTO(null, "Bad Project", "Desc", Set.of(99L));

        when(developerRepository.findById(3L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectServices.createProject(invalidDTO);
        }, "Should throw an exception if a developer is not found.");

        assertTrue(exception.getMessage().contains("Developer not found: 99"), "Exception message should indicate the missing developer ID.");

        verify(developerRepository, times(1)).findById(99L);
        verify(projectRepository, never()).save(any(Project.class));
    }
}
*/