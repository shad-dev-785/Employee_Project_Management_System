package com.agile.controller;
import com.agile.dto.ResponseDto;
import com.agile.dto.SprintUserRequestDto;
import com.agile.service.SprintUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping(path = "/api/sprint-user")
public class SprintUserController {
    @Autowired
    SprintUserService sprintUserService;

    @PostMapping("/add-sprint-user")
    public ResponseDto addUserToSprint(@RequestBody SprintUserRequestDto sprintUserRequestDto){
        return sprintUserService.addUserToSprint(sprintUserRequestDto);
    }
    @GetMapping("/get-sprints")
    public ResponseDto getSprintsFromProjectId(@RequestParam Long projectId){
        return sprintUserService.getSprintsFromProjectId(projectId);
    }
    @GetMapping("/get-sprint-with-user")
    public ResponseDto getSprintWithUsers(@RequestParam Long sprintId){
        return sprintUserService.getSprintWithUsers(sprintId);
    }
}
