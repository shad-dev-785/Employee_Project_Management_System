package com.agile.repository;

import com.agile.model.UserLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Repository
public interface UserLeaveRepository extends JpaRepository<UserLeave, Long> {
    @Query("SELECT u FROM UserLeave u WHERE u.userId =:id")
    Optional<UserLeave> findByuserId(@Param("id") Long id);
}
