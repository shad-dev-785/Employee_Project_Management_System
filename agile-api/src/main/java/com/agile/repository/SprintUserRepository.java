package com.agile.repository;


import com.agile.model.SprintUser;
import com.agile.model.SprintUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SprintUserRepository extends JpaRepository<SprintUser, SprintUserId> {
    @Query("SELECT su FROM SprintUser su WHERE su.id.userId = :userId")
    List<SprintUser> findByUserId(@Param("userId") Long userId);
    @Query("SELECT su FROM SprintUser su WHERE su.id.userId = :userId AND su.id.sprintId = :sprintId")
    Optional<SprintUser> findBySprintId(@Param("sprintId") Long sprintId, @Param("userId") Long userId);
    @Query("SELECT su FROM SprintUser su WHERE su.id.sprintId = :sprintId")
    List<SprintUser> findBySprintIdOnly(@Param("sprintId") Long sprintId);
}
