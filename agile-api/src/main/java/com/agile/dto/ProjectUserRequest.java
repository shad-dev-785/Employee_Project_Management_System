package com.agile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUserRequest {
    private Long userId;
    private Long projectId;
    private boolean isAdmin;
}
