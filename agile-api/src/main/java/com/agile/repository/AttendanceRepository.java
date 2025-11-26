package com.agile.repository;

import com.agile.model.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    @Query("SELECT a FROM Attendance a WHERE a.userId = :userId AND a.date = :date ORDER BY a.date DESC")
    Optional<Attendance> findByUserIdAndDate(@Param("userId") Long userId, @Param("date")LocalDate date);
    @Query("SELECT a FROM Attendance a WHERE a.userId = :userId " +
        "AND a.date BETWEEN :startDate AND :endDate ORDER BY a.date DESC")
    Page<Attendance> findByUserIdAndDateBetween(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageable") Pageable pageable);
    @Query("SELECT a FROM Attendance a WHERE a.userId = :userId ORDER BY a.date DESC")
    Page<Attendance> findALLByUserId(@Param("userId") Long userId, @Param("pageable") Pageable pageable);
    @Query("SELECT a FROM Attendance a ORDER BY a.date ASC")
    Page<Attendance> findAllAttendances(@Param("pageable") Pageable pageable);
    @Query("SELECT a FROM Attendance a WHERE date BETWEEN :startDate AND :endDate ORDER BY a.date ASC")
    Page<Attendance> findAttendanceBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageable") Pageable pageable);
}
