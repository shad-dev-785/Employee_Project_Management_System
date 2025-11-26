package com.agile.repository;

import com.agile.model.Timesheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.loader.Loader.SELECT;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet,Long> {

    @Query("SELECT t FROM Timesheet t WHERE t.taskId IN :taskIdList")
    Page<Timesheet> findByTaskIds(@Param("taskIdList") List<Long> taskIdList, Pageable pageable);
    @Query("SELECT t FROM Timesheet t WHERE t.taskId = :taskId")
    List<Timesheet> findAllByTaskId(@Param("taskId") Long taskId);

    @Query("SELECT t FROM Timesheet t " +
        "WHERE t.createdAt BETWEEN :startDate AND :endDate " +
        "AND t.taskId IN :taskIds")
    List<Timesheet> findTimesheetsByStartDateBetweenAndTaskIds(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("taskIds") List<Long> taskIds
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM Timesheet t WHERE t.taskId = :taskId")
    void deleteByTaskId(@Param("taskId") Long taskId);

    @Query("SELECT t FROM Timesheet t WHERE t.commentDescription like :commentDescription ORDER BY t.createdAt DESC")
    Optional<Timesheet> findByCommentDesc(@Param("commentDescription") String commentDescription);
    @Query("SELECT t FROM Timesheet t WHERE t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    Page<Timesheet> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT t FROM Timesheet t WHERE t.createdAt BETWEEN :startDate AND :endDate AND t.owner LIKE :userName ORDER BY t.createdAt DESC")
    Page<Timesheet> findByCreatedBetweenAndUserName(LocalDateTime startDate, LocalDateTime endDate, String userName,Pageable pageable);

}
