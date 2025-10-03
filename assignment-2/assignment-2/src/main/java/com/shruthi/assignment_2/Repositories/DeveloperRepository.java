package com.shruthi.assignment_2.Repositories;

import com.shruthi.assignment_2.Model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    @Query("SELECT d FROM Developer d JOIN FETCH d.projects p WHERE p.id = :projectId")
    List<Developer> findDevelopersByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.developer.id = :developerId AND t.status = 'IN_PROGRESS'")
    int countInProgressTasks(@Param("developerId") Long developerId);

    @Query(value = """
    SELECT d.id, d.name, COUNT(t.id) AS overdue_count
    FROM developer d
    JOIN task t ON d.id = t.developer_id
    WHERE t.due_date < CURRENT_DATE AND t.status != 'DONE'
    GROUP BY d.id, d.name
    ORDER BY overdue_count DESC
    LIMIT 3
    """, nativeQuery = true)
    List<Object[]> findTop3DevelopersWithMostOverdueTasks();

}
