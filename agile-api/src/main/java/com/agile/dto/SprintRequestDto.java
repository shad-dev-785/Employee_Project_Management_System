package com.agile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class SprintRequestDto {
    private String name;
    private Long projectId;
    private boolean Static;
    private LocalDate startDate;
    private LocalDate endDate;
}
