package com.agile.project_service.repository;

import com.agile.project_service.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> , JpaSpecificationExecutor<Project> {
    Boolean existsByName(String name);

    List<Project> findByMemberIdsContaining(Long userId);
}
