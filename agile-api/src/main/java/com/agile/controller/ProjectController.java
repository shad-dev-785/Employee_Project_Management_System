package com.agile.controller;

import com.agile.dto.ProjectRequestDto;
import com.agile.dto.ResponseDto;
import com.agile.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping(path = "/api/project")
public class ProjectController {
    @Autowired
    ProjectService projectService;


    @PostMapping("/add-project")
    public ResponseDto addProject(@RequestBody ProjectRequestDto projectRequestDto){
        log.info("Entering add-project {}", projectRequestDto.toString());
        return projectService.addProject(projectRequestDto);
    }

    @PostMapping("/update")
    public ResponseDto updateProject(@RequestParam Long projectId, @RequestBody ProjectRequestDto projectRequestDto){
        log.info("Entering update-project {}", projectRequestDto.toString());
        return projectService.updateProject(projectId ,projectRequestDto);
    }

    @GetMapping("/get-project-with-users")
    public ResponseDto getProjectWithUsers(@RequestParam Long projectId){
        return projectService.getProjectWithUsers(projectId);
    }

    @GetMapping("/get-project-from-id")
    public ResponseDto getProjectById(@RequestParam Long id){
        return projectService.getProjectById(id);
    }

}
//  todo- getProject, updateProject, addUser, removeUser
//  todo- validateNAME,
