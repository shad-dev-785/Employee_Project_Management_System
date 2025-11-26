package com.agile.repository;

import com.agile.model.Project;
import com.agile.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.title like :title")
    Optional<Task> findByName(@Param("title") String title);
    @Query("SELECT t FROM Task t WHERE t.sprint.id= :sprintId")
    List<Task> getAllFromSprintId(@Param("sprintId") Long sprintId);
    @Query("SELECT t FROM Task t WHERE t.id IN :taskIdList")
    List<Task> getAllFromTaskIds(@Param("taskIdList") List<Long> taskIdList);
    @Query("SELECT t FROM Task t WHERE t.project.id= :projectId")
    List<Task> findByProjectId(@Param("projectId") Long projectId);
}
