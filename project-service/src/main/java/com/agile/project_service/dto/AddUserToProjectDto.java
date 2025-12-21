package com.agile.project_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddUserToProjectDto {
    private List<Long> userIds;
}
