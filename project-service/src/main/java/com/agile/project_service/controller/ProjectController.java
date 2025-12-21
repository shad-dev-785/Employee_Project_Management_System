package com.agile.project_service.controller;

import com.agile.dto.ResponseDto;
import com.agile.project_service.dto.AddUserToProjectDto;
import com.agile.project_service.dto.ProjectDto;
import com.agile.project_service.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/projects")
@Slf4j
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @PostMapping("/create")
    public ResponseDto createProject(@RequestBody ProjectDto dto) {
        log.info("Creating a new project");
        return projectService.createProject(dto);
    }
    @PostMapping("/{projectId}/add-members")
    public ResponseDto addMembersToProject(@PathVariable Long id, @RequestBody AddUserToProjectDto dto) {
        log.info("Adding members to project");
        return projectService.addMembersToProject(dto);
    }
}
