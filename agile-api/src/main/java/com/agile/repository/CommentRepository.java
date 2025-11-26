package com.agile.repository;

import com.agile.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.task.id= :taskId")
    List<Comment> getByTaskId(@Param("taskId") Long taskId);

    @Modifying
    @Transactional
    @Query("delete from Comment c where c.task.id = ?1")
    void deleteAllByTaskId(@Param("taskId") Long taskId);
}
