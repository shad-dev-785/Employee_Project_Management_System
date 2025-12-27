package com.agile.project_service.controller;

import com.agile.dto.ResponseDto;
import com.agile.project_service.dto.SprintDto;
import com.agile.project_service.service.SprintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/sprints")
public class SprintController {
    @Autowired
    private SprintService sprintService;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/create")
    public ResponseDto createSprint(@RequestBody SprintDto sprintDto) {
        log.info("Creating a new sprint");
//         Implementation for creating a sprint
        return sprintService.createSprint(sprintDto);
    }
    @PostMapping("/assign-members")
    public ResponseDto assignMembersToSprint(@RequestBody SprintDto sprintDto) {
        // Implementation for assigning members to a sprint
        return sprintService.assignMembersToSprint(sprintDto);
    }
}
