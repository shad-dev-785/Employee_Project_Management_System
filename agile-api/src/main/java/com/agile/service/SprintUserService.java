package com.agile.service;

import com.agile.UserType;
import com.agile.constant.Constants;
import com.agile.dto.ResponseDto;
import com.agile.dto.SprintUserRequestDto;
import com.agile.dto.SprintWithUserResponse;
import com.agile.model.*;
import com.agile.repository.*;
import com.agile.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SprintUserService {
    @Autowired
    private SprintUserRepository sprintUserRepository;
    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectUserRepository projectUserRepository;

    public ResponseDto addUserToSprint(SprintUserRequestDto sprintUserRequestDto) {
        ResponseDto responseDto= new ResponseDto();
        Utility utility= new Utility();
        String userName= utility.getUserName();
        Sprint sprint= sprintRepository.findById(sprintUserRequestDto.getSprintId()).orElse(null);
        if(sprint==null){
            responseDto.setMessage("Sprint not found!");
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setStatus(Constants.FAILED);
            responseDto.setData(null);
            return responseDto;
        }
        Optional<User> user= Optional.ofNullable(userRepository.findByEmail(userName));
        Long projectId= sprint.getProject().getId();


        SprintUser sprintUser2= sprintUserRepository.findBySprintId(sprintUserRequestDto.getSprintId(), sprintUserRequestDto.getUserId()).orElse(null);
               if(sprintUser2!=null){
                   responseDto.setMessage("User already present in Sprint!");
                   responseDto.setStatusCode(Constants.FAILED_CODE);
                   responseDto.setStatus(Constants.FAILED);
                   responseDto.setData(null);
                   return responseDto;
               }
               SprintUser sprintUser1= new SprintUser();
               SprintUserId sprintUserId= new SprintUserId();
               sprintUserId.setSprintId(sprintUserRequestDto.getSprintId());
               sprintUserId.setUserId(sprintUserRequestDto.getUserId());
               sprintUser1.setId(sprintUserId);
               SprintUser savedSprintUser= sprintUserRepository.save(sprintUser1);
               responseDto.setData(savedSprintUser);
               responseDto.setMessage("User successfully added to sprint");
               return responseDto;

    }
    public ResponseDto getSprintsFromProjectId(Long projectId) {
        ResponseDto responseDto = new ResponseDto();
        Utility utility = new Utility();
        String userName = utility.getUserName();
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userName));
        List<Sprint> sprintList= sprintRepository.getAllByProjectId(projectId);
        if(user.get().getType().equals(UserType.ADMIN)){
            responseDto.setData(sprintList);
            responseDto.setMessage("Sprints");
            return responseDto;
        }
//        ProjectUser projectUser= projectUserRepository.findByProjectId(projectId, user.get().getId()).orElse(null);
        List<SprintUser> sprintUserList= sprintUserRepository.findByUserId(user.get().getId());
        List<Long> sprintIds= new ArrayList<>();
        for(SprintUser s:sprintUserList){
                sprintIds.add(s.getId().getSprintId());
        }

        if(sprintIds.size()>0){
            List<Sprint> sprintsList= sprintRepository.findAllById(sprintIds);
            List<Sprint> sprints= new ArrayList<>();
            for(Sprint s: sprintList){
                if(s.getProject().getId()==projectId){
                    sprints.add(s);
                }
            }
            responseDto.setData(sprints);
            responseDto.setMessage("Sprints");
            return responseDto;
        }

        responseDto.setData(null);
        responseDto.setStatus(Constants.FAILED);
        responseDto.setMessage("No Sprints found for this project for User!");
        responseDto.setStatusCode(Constants.FAILED_CODE);
        return responseDto;
    }

    public ResponseDto getSprintWithUsers(Long sprintId) {
        Utility utility= new Utility();
        String userName= utility.getUserName();
        SprintWithUserResponse sprintWithUserResponse= new SprintWithUserResponse();
        Sprint sprint= sprintRepository.findById(sprintId).orElse(null);
        ResponseDto responseDto= new ResponseDto();
        List<Long> assigneeIds= new ArrayList<>();
        List<SprintUser> sprintUserList= sprintUserRepository.findBySprintIdOnly(sprintId);
        for(SprintUser s: sprintUserList){
            assigneeIds.add(s.getId().getUserId());
        }
        if(assigneeIds.size()>0) {
            List<User> assignees = userRepository.getAllUsersByIds(assigneeIds);
            sprintWithUserResponse.setSprint(sprint);
            sprintWithUserResponse.setAssignees(assignees);
            responseDto.setData(sprintWithUserResponse);
            responseDto.setMessage("sprint with users assigned");
            return responseDto;
        }
        sprintWithUserResponse.setSprint(sprint);
        List<User> assignees1= new ArrayList<>();
        sprintWithUserResponse.setAssignees(assignees1);
        responseDto.setData(sprintWithUserResponse);
        responseDto.setMessage("No users added in the sprint!");
        return responseDto;
    }
}
