package com.agile.service;


import com.agile.UserType;
import com.agile.constant.Constants;
import com.agile.dto.ResponseDto;
import com.agile.dto.SprintRequestDto;
import com.agile.model.*;
import com.agile.repository.*;
import com.agile.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SprintService {
    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SprintUserRepository sprintUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    public ResponseDto createSprint(SprintRequestDto sprintRequestDto) {
        ResponseDto responseDto= new ResponseDto();
        Utility utility= new Utility();
        String userName= utility.getUserName();
        Optional<User> user= Optional.ofNullable(userRepository.findByEmail(userName));
        Sprint sprint= sprintRepository.findByName(sprintRequestDto.getName()).orElse(null);
        Project project= projectRepository.findById(sprintRequestDto.getProjectId()).orElse(null);
            if (sprint == null) {
                Sprint sprint1 = new Sprint();
                sprint1.setName(sprintRequestDto.getName());
                sprint1.setProject(project);
                sprint1.setCreatedBy(user.get().getUsername());
                sprint1.setCreatedOn(LocalDate.now());
                sprint1.setStartDate(sprintRequestDto.getStartDate());
                sprint1.setEndDate(sprintRequestDto.getEndDate());
                sprint1.setStatic(sprintRequestDto.isStatic());
                Sprint savedSprint = sprintRepository.save(sprint1);
                SprintUser sprintUser = new SprintUser();
                SprintUserId sprintUserId = new SprintUserId();
                sprintUserId.setUserId(user.get().getId());
                sprintUserId.setSprintId(savedSprint.getId());
                sprintUser.setId(sprintUserId);

                sprintUserRepository.save(sprintUser);
                responseDto.setMessage("Sprint added successfully");
                responseDto.setData(savedSprint);
                return responseDto;
            }
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setStatus(Constants.FAILED);
            responseDto.setMessage("Sprint already exists!");
            return responseDto;

    }

    public ResponseDto updateSprint(Long sprintId, SprintRequestDto sprintRequestDto) {
        ResponseDto responseDto= new ResponseDto();
        Utility utility= new Utility();
        String userName= utility.getUserName();
        Optional<User> user= Optional.ofNullable(userRepository.findByEmail(userName));
        Sprint sprint= sprintRepository.findById(sprintId).orElse(null);
        Project project= projectRepository.findById(sprint.getProject().getId()).orElse(null);
        if(sprint==null){
            responseDto.setMessage("Sprint doesn't exist!");
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setStatus(Constants.FAILED);
            responseDto.setData(null);
            return responseDto;
        }
        if(project==null){
            responseDto.setMessage("Project doesn't exist!");
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setStatus(Constants.FAILED);
            responseDto.setData(null);
            return responseDto;
        }
        if(user.get().getUsername().equals(sprint.getCreatedBy())){
            sprint.setName(sprintRequestDto.getName());
            sprint.setUpdatedBy(user.get().getUsername());
            sprint.setEndDate(sprintRequestDto.getEndDate());
            sprint.setStatic(sprintRequestDto.isStatic());
            sprint.setUpdatedDate(LocalDate.now());
            Sprint savedSprint= sprintRepository.save(sprint);
            SprintUser sprintUser= new SprintUser();
            SprintUserId sprintUserId= new SprintUserId();
            sprintUserId.setUserId(user.get().getId());
            sprintUserId.setSprintId(savedSprint.getId());
            sprintUser.setId(sprintUserId);
            sprintUserRepository.save(sprintUser);
            responseDto.setData(savedSprint);
            responseDto.setMessage("Sprint successfully Updated");
            return responseDto;
        }
        responseDto.setStatusCode(Constants.FAILED_CODE);
        responseDto.setStatus(Constants.FAILED);
        responseDto.setMessage("Not Allowed!");
        return responseDto;
    }

    public ResponseDto getSprintById(Long id) {
        ResponseDto responseDto= new ResponseDto();
        Sprint sprint= sprintRepository.findById(id).orElse(null);
        if(sprint==null){
            responseDto.setMessage("Sprint Doesn't Exists!");
            responseDto.setStatus(Constants.FAILED);
            responseDto.setStatusCode(Constants.FAILED_CODE);
        }else{
            responseDto.setData(sprint);
            responseDto.setMessage("Sprint");
        }
        return responseDto;
    }
}
