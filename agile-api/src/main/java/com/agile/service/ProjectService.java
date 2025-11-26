package com.agile.service;

import com.agile.constant.Constants;
import com.agile.dto.ProjectRequestDto;
import com.agile.dto.ProjectResponse;
import com.agile.dto.ResponseDto;
import com.agile.model.*;
import com.agile.repository.*;
import com.agile.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectUserRepository projectUserRepository;
    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private TaskRepository taskRepository;

    public ResponseDto addProject(ProjectRequestDto projectRequestDto) {
        Utility utility= new Utility();
        String userName= utility.getUserName();

        ResponseDto responseDto= new ResponseDto();
        Project project1= projectRepository.findByProjectName(projectRequestDto.getName()).orElse(null);
        User user= userRepository.findByEmail(userName);
        if(user== null|| project1!=null){
            responseDto.setStatus(Constants.FAILED);
            if(project1!=null){
                responseDto.setMessage("Project already exists");
                responseDto.setStatusCode(Constants.FAILED_CODE);
            }else {
                responseDto.setMessage(Constants.USER_NOT_FOUND);
                responseDto.setStatusCode(Constants.USER_NOT_FOUND_CODE);
            }
            return responseDto;
        }
        Project project= new Project();
        project.setName(projectRequestDto.getName());
        project.setCreatedBy(user.getUsername());
        Project savedProject= projectRepository.save(project);
        ProjectUser projectUser= new ProjectUser();
        ProjectUserId projectUserId= new ProjectUserId();
        projectUserId.setProjectId(savedProject.getId());
        projectUserId.setUserId(user.getId());
        projectUser.setId(projectUserId);
        projectUserRepository.save(projectUser);
//        todo - add projectUser in projectUser Table with isadmin==true.
        responseDto.setMessage(Constants.SUCCESS);
        responseDto.setData(savedProject);
        return responseDto;
    }

    public ResponseDto updateProject(Long projectId, ProjectRequestDto projectRequestDto) {
        Utility utility= new Utility();
        String userName= utility.getUserName();
        Project project= projectRepository.findById(projectId).orElse(null);
        ResponseDto responseDto= new ResponseDto();
        if(project==null){
            responseDto.setMessage("Project not Found!");
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setStatus(Constants.FAILED);
            responseDto.setData(projectRequestDto);
            return responseDto;
        }
        User user= userRepository.findByEmail(userName);
        if(user==null){
            responseDto.setMessage(Constants.USER_NOT_FOUND);
            responseDto.setStatusCode(Constants.USERNAME_NOT_UNIQUE_CODE);
            responseDto.setStatus(Constants.USER_NOT_FOUND);
            responseDto.setData(projectRequestDto);
            return responseDto;
        }
        project.setName(projectRequestDto.getName());
        project.setUpdatedBy(user.getUsername());
        Project savedProject=projectRepository.save(project);
        ProjectUser projectUser= new ProjectUser();
        ProjectUserId projectUserId= new ProjectUserId();
        projectUserId.setProjectId(savedProject.getId());
        projectUserId.setUserId(user.getId());
        projectUser.setId(projectUserId);
        responseDto.setMessage(Constants.SUCCESS);
        responseDto.setData(savedProject);
        return responseDto;
    }
    public ResponseDto getProjectWithUsers(Long projectId){
        ResponseDto responseDto= new ResponseDto();
        ProjectResponse response= new ProjectResponse();
        Project project= projectRepository.findById(projectId).orElse(null);
        response.setProject(project);
        List<Long> userIdList= new ArrayList<>();
        List<ProjectUser> projectUserList= projectUserRepository.findByProjectIdOnly(projectId);
        for(ProjectUser p: projectUserList){
            userIdList.add(p.getId().getUserId());
        }
        List<User> usersList= userRepository.getAllUsersByIds(userIdList);
        response.setUsersInProject(usersList);
        responseDto.setData(response);
        responseDto.setMessage("Project with its users");
        return responseDto;

    }


    public ResponseDto getProjectById(Long id) {
        ResponseDto responseDto= new ResponseDto();
        Project project= projectRepository.findById(id).orElse(null);
        if(project==null){
            responseDto.setMessage("Project Doesn't Exists!");
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setStatus(Constants.FAILED);
        }else{
            responseDto.setStatus(Constants.SUCCESS);
            responseDto.setStatusCode(Constants.SUCCESS_CODE);
            responseDto.setData(project);
            responseDto.setMessage("Project");
        }
        return responseDto;
    }
}
