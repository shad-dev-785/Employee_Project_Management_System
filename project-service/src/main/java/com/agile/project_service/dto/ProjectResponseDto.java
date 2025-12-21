package com.agile.project_service.dto;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class ProjectResponseDto {
    private Long id;
    private String name;
    private String description;
    private Set<Integer> memberIds;
}
