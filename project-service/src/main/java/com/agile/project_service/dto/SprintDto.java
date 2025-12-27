package com.agile.project_service.dto;
import lombok.Data;

import java.util.Set;

@Data
public class SprintDto {
    private Long id;
    private String name;
    private String description;
    private Long projectId;
    private Set<Long> memberIds;
}
