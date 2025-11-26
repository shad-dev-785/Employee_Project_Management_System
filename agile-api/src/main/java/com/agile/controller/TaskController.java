package com.agile.controller;

import com.agile.dto.ResponseDto;
import com.agile.dto.TaskRequestDto;
import com.agile.dto.TaskResponseDto;
import com.agile.dto.TaskWithCommentDto;
import com.agile.model.Comment;
import com.agile.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping(path = "/api/task")
public class TaskController {
    @Autowired
    TaskService taskService;
    @PostMapping("/add-task")
    public ResponseDto addTask(@RequestBody TaskWithCommentDto taskWithCommentDto){
        return taskService.addTask(taskWithCommentDto);
    }
    @PostMapping("/update")
    public ResponseDto updateTask(@RequestParam Long taskId, @RequestBody TaskRequestDto taskRequestDto){
        return taskService.updateTask(taskId, taskRequestDto);
    }
    @DeleteMapping("/delete")
    public ResponseDto deleteTask(@RequestParam Long taskId){
        return taskService.deleteTask(taskId);
    }

    @GetMapping("/get-task-by-id")
    public ResponseDto getTaskById(@RequestParam Long id){
        return taskService.getTaskById(id);
    }
}
