package com.agile.controller;

import com.agile.dto.ProjectUserRequest;
import com.agile.dto.ResponseDto;
import com.agile.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping(path= "/api/project-user")
public class ProjectUserController {
    @Autowired
    ProjectUserService projectUserService;
    @PostMapping("/add-user")
    public ResponseDto addUserToProject(@RequestBody ProjectUserRequest projectUserRequest){
        return projectUserService.addProjectUser(projectUserRequest);
    }
    @GetMapping("/get-projects")
    public ResponseDto getProjectFromUserId(@RequestParam Long userId){
        return projectUserService.getProjectFromUserId(userId);
    }
    @GetMapping("/get-project-with-users")
    public ResponseDto getProjectWithUsers(@RequestParam Long projectId){
        return projectUserService.getProjectWithUsers(projectId);
    }

}
