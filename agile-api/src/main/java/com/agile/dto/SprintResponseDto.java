package com.agile.dto;

import com.agile.model.Project;
import java.time.LocalDate;

public class SprintResponseDto {
    private Long id;


    private LocalDate startDate;


    private LocalDate endDate;


    private LocalDate updatedDate;


    private String name;


    private boolean Static;


    private String createdBy;


    private LocalDate createdOn;


    private String updatedBy;

    private Project project;

    private double totalUsed;
}
