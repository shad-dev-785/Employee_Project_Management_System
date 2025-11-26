package com.agile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimesheetResponseDto {
    private Long id;


    private String owner;


    private Long taskId;


    private String taskDescription;

    private String projectDescription;


    private String commentDescription;


    private double used;


    private LocalDateTime createdAt;

}
