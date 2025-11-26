package com.agile.controller;

import com.agile.dto.CommentRequestDto;
import com.agile.dto.ResponseDto;
import com.agile.dto.TimesheetResponse;
import com.agile.dto.TimesheetWithFilterDto;
import com.agile.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@Slf4j
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping(path = "/api/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("/add-comment")
    public ResponseDto addComment(@RequestBody CommentRequestDto commentRequestDto){
        return commentService.addComment(commentRequestDto);
       }
       @PutMapping("/update")
       public ResponseDto updateComment(@RequestParam Long commentId, @RequestBody CommentRequestDto commentRequestDto){
        return commentService.updateComment(commentId, commentRequestDto);
       }
       @GetMapping("/get-comments")
        public ResponseDto getComments(@RequestParam Long taskId){
         return commentService.getComments(taskId);
    }

    @GetMapping("/get-timesheet")
    public ResponseDto getTimesheet(@RequestBody TimesheetResponse response){
        return commentService.getTimesheet(response);
    }
    @GetMapping("/get-all-timesheet")
    public ResponseDto getAllTimesheet(@RequestBody TimesheetResponse response){
        return commentService.getAllTimesheet(response);
    }

    @DeleteMapping("/delete")
    public ResponseDto deleteComment(@RequestParam Long commentId){
        return commentService.deleteComment(commentId);
    }

}

