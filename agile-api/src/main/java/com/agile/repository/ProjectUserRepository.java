package com.agile.repository;

import com.agile.model.ProjectUser;
import com.agile.model.ProjectUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, ProjectUserId> {
    @Query("SELECT p FROM ProjectUser p WHERE p.id.userId= :userId")
    List<ProjectUser> getAllById(@Param("userId") Long userId);
    @Query("SELECT p FROM ProjectUser p WHERE p.id.userId= :userId")
    Optional<ProjectUser> findByUserId(@Param("userId") Long userId);
    @Query("SELECT p FROM ProjectUser p WHERE p.id.projectId = :projectId AND p.id.userId = :userId")
    Optional<ProjectUser> findByProjectId(@Param("projectId") Long projectId, @Param("userId") Long userId);
    @Query("SELECT p FROM ProjectUser p WHERE p.id.projectId = :projectId")
    List<ProjectUser> findByProjectIdOnly(@Param("projectId") Long projectId);
}
