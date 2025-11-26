package com.agile.dto;

import com.agile.Priority;
import lombok.Data;
import javax.persistence.Column;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Data
public class TaskRequestDto {
    @Column(name = "sprint_id")
    private Long sprintId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String owner;
    private String title;
    private Priority priority;
    private double estimated;
    private LocalTime runningTime;

}
