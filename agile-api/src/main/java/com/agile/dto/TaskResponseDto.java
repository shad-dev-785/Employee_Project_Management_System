package com.agile.dto;

import com.agile.Priority;
import com.agile.Status;
import com.agile.model.Project;
import com.agile.model.Sprint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskResponseDto {
    private Long id;


    private String owner;


    private String title;


    private LocalDateTime createdAt;


    private String createdBy;


    private LocalDateTime updatedAt;


    private String updatedBy;


    private Priority priority;


    private double estimated;


    private LocalTime runningTime;


    private Sprint sprint;


    private Project project;

    private double totalUsed;

    private Status status;

}
