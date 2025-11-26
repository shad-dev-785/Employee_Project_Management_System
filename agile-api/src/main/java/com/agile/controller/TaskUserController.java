package com.agile.controller;

import com.agile.dto.ResponseDto;
import com.agile.dto.TaskUserDto;
import com.agile.model.TaskUser;
import com.agile.model.TaskUserId;
import com.agile.service.TaskUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping(path = "/api/task-user")
public class TaskUserController {
    @Autowired
    TaskUserService taskUserService;

//    @PostMapping("/add-user-to-task")
//    public ResponseDto addUserToTask(@RequestBody TaskUserDto taskUserDto){
//        return taskUserService.addUserToTask(taskUserDto);
//    }
    @GetMapping("/get-tasks")
    public ResponseDto getTasks(@RequestParam Long sprintId){
        return taskUserService.getTasks(sprintId);
    }
}
