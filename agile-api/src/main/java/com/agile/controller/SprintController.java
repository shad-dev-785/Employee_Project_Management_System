package com.agile.controller;

import com.agile.dto.ResponseDto;
import com.agile.dto.SprintRequestDto;
import com.agile.service.SprintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping(path = "/api/sprint")
public class SprintController {
    @Autowired
    SprintService sprintService;
    @PostMapping("/create-sprint")
    public ResponseDto createSprint(@RequestBody SprintRequestDto sprintRequestDto){
        return sprintService.createSprint(sprintRequestDto);
    }
    @PostMapping("/update-sprint")
    public ResponseDto updateSprint(@RequestParam Long sprintId, @RequestBody SprintRequestDto sprintRequestDto){
        return sprintService.updateSprint(sprintId, sprintRequestDto);
    }

    @GetMapping("/get-sprint-by-id")
    public ResponseDto getSprintById(@RequestParam Long id){
        return sprintService.getSprintById(id);
    }
}
