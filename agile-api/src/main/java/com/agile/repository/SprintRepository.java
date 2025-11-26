package com.agile.repository;


import com.agile.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
    @Query("SELECT s FROM Sprint s WHERE s.name like :name")
    Optional<Sprint> findByName(@Param("name") String name);
     @Query("Select s FROM Sprint s WHERE s.project.id =:projectId")
    List<Sprint> getAllByProjectId(@Param("projectId") Long projectId);
}
