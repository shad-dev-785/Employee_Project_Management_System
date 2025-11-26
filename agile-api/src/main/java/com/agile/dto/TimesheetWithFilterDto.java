package com.agile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimesheetWithFilterDto {
    private Long projectId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String userName;
}
