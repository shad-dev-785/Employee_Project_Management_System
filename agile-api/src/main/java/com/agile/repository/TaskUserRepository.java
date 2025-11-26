package com.agile.repository;

import com.agile.model.TaskUser;
import com.agile.model.TaskUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Repository
public interface TaskUserRepository extends JpaRepository<TaskUser, TaskUserId> {
    @Query("SELECT tu FROM TaskUser tu WHERE tu.id.taskId = :taskId")
    Optional<TaskUser> findByTaskId(@Param("taskId") Long taskId);
    @Query("SELECT tu FROM TaskUser tu WHERE tu.id.userId = :userId")
    List<TaskUser> findByUserId(@Param(("userId")) Long userId);
    @Query("SELECT tu FROM TaskUser tu WHERE tu.id.userId = :userId AND tu.id.taskId= :taskId")
    Optional<TaskUser> findByTaskAndUserId(@Param("taskId") Long taskId, @Param("userId") Long userId);
    @Modifying
    @Transactional
    @Query("delete from TaskUser tu where tu in ?1")
    void deleteAllByTaskId(@Param("taskUsers") List<TaskUser> taskUsers);

    @Modifying
    @Transactional
    @Query("delete from TaskUser tu where tu.id.taskId= :taskId")
    void deleteTask(@Param("taskId") Long taskId);
}
