package com.agile.repository;

import com.agile.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    @Query("SELECT l FROM Leave l WHERE l.approvedBy like :userName")
    List<Leave> findAllByApprovedBy(@Param("userName") String userName);
    @Query("SELECT l FROM Leave l WHERE l.user.id = :id")
    List<Leave> finalAllByUerId(@Param("id") Long id);
    @Query("SELECT l FROM Leave l WHERE l.user.id = ?1 AND ?2 BETWEEN l.startDate AND l.endDate")
    Optional<Leave> findByUserIdAndDate(@Param("id") Long id, @Param("date") LocalDate date);
}
