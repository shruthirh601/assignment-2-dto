package com.shruthi.assignment_2.Repositories;

import com.shruthi.assignment_2.Model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.developers WHERE p.id = :id")
    Optional<Project> findProjectWithDevelopersById(@Param("id") Long id);
}
