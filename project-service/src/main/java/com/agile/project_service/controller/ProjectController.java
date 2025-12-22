package com.agile.project_service.controller;

import com.agile.dto.ResponseDto;
import com.agile.project_service.dto.AddUserToProjectDto;
import com.agile.project_service.dto.ProjectDto;
import com.agile.project_service.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/create")
    public ResponseDto createProject(@RequestBody ProjectDto dto) {
        log.info("Creating a new project");
        return projectService.createProject(dto);
    }
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/{id}/add-members")
    public ResponseDto addMembersToProject(@PathVariable Long id, @RequestBody AddUserToProjectDto dto) {
        log.info("Adding members to project");
        return projectService.addMembersToProject(id,dto);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/find-all")
    public ResponseDto getAllProjects() {
        log.info("Fetching all projects");
        return projectService.getAllProjects();
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER, ROLE_DEVELOPER, ROLE_HR')")
    @PostMapping("/fine-project-by-user")
    public ResponseDto findProjectsByUserId(@RequestParam Long userId) {
        log.info("Finding projects for user id: " + userId);
        return projectService.findProjectsByUserId(userId);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/{id}/remove-member")
    public ResponseDto removeMemberFromProject(@PathVariable Long id, @RequestParam Long userId) {
        log.info("Removing member from project");
        return projectService.removeMemberFromProject(id, userId);
    }


}
