package com.agile.service;

import com.agile.UserType;
import com.agile.constant.Constants;
import com.agile.dto.*;
import com.agile.model.*;
import com.agile.repository.ProjectRepository;
import com.agile.repository.ProjectUserRepository;
import com.agile.repository.UserRepository;
import com.agile.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectUserService {
    @Autowired
    private ProjectUserRepository projectUserRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseDto addProjectUser(ProjectUserRequest projectUserRequest) {
        ResponseDto responseDto= new ResponseDto();
        Utility utility= new Utility();
        String userName= utility.getUserName();
        Project project= projectRepository.findById(projectUserRequest.getProjectId()).orElse(null);
        if(project==null){
            responseDto.setMessage("Project not Found!");
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setStatus(Constants.FAILED);
            responseDto.setData(null);
            return responseDto;
        }
        User user= userRepository.findById(projectUserRequest.getUserId()).orElse(null);
        if(user==null){
            responseDto.setMessage(Constants.USER_NOT_FOUND);
            responseDto.setStatusCode(Constants.USERNAME_NOT_UNIQUE_CODE);
            responseDto.setStatus(Constants.USER_NOT_FOUND);
            responseDto.setData(null);
            return responseDto;
        }
        User user1= userRepository.findByEmail(userName);
        ProjectUser projectUser= projectUserRepository.findByProjectId(projectUserRequest.getProjectId(), projectUserRequest.getUserId()).orElse(null);
        //use utilityclass to get and validate username and project admin.
        //search projectUser if exists with submittedbyId as userId, projectId, isAdmin==true.
        if(user1.getType().equals(UserType.ADMIN)) {
            if(projectUser!=null){
                responseDto.setMessage("User already present in Project!");
                responseDto.setStatusCode(Constants.FAILED_CODE);
                responseDto.setStatus(Constants.FAILED);
                responseDto.setData(null);
                return responseDto;
            }
                ProjectUser projectUser2 = new ProjectUser();
                ProjectUserId projectUserId = new ProjectUserId();
                projectUserId.setProjectId(project.getId());
                projectUserId.setUserId(user.getId());
                projectUser2.setId(projectUserId);
                ProjectUser savedProjectUser = projectUserRepository.save(projectUser2);
                responseDto.setMessage("User added to Project");
                responseDto.setData(savedProjectUser);
                return responseDto;
        }
        responseDto.setMessage("Failed to add user");
        responseDto.setStatusCode(Constants.FAILED_CODE);
        responseDto.setStatus(Constants.FAILED);
        responseDto.setData(null);
        return responseDto;
    }
    public ResponseDto getProjectFromUserId(Long userId){
        ResponseDto responseDto= new ResponseDto();
        List<ProjectUser> projectUsers= projectUserRepository.getAllById(userId);
       List<Long> projectIdList= new ArrayList<>();
        for(ProjectUser p: projectUsers){
            projectIdList.add(p.getId().getProjectId());
        }
        if(projectIdList.isEmpty()){
            responseDto.setMessage("No Projects for this user!");
            responseDto.setStatus(Constants.FAILED);
            responseDto.setStatusCode(Constants.FAILED_CODE);
            return responseDto;
        }
        List<Project> projectList= projectRepository.findProjectsByIds(projectIdList);
        responseDto.setData(projectList);
        responseDto.setMessage("Projects");
        return responseDto;
    }
    public ResponseDto getProjectWithUsers(Long projectId) {
        Utility utility= new Utility();
        String userName= utility.getUserName();
        ProjectResponse projectResponse= new ProjectResponse();
        Project project= projectRepository.findById(projectId).orElse(null);
        ResponseDto responseDto= new ResponseDto();
        List<Long> assigneeIds= new ArrayList<>();
        List<ProjectUser> projectUserList= projectUserRepository.findByProjectIdOnly(projectId);
        for(ProjectUser p: projectUserList){
            assigneeIds.add(p.getId().getUserId());
        }
        if(assigneeIds.size()>0) {


            List<User> assignees = userRepository.getAllUsersByIds(assigneeIds);
            projectResponse.setProject(project);
            projectResponse.setUsersInProject(assignees);
            responseDto.setData(projectResponse);
            responseDto.setMessage("Project with users assigned");
            return responseDto;
        }
        projectResponse.setProject(project);
        List<User> assignees1= new ArrayList<>();
        projectResponse.setUsersInProject(assignees1);
        responseDto.setData(projectResponse);
        responseDto.setMessage("No users added in the Project!");
        return responseDto;
    }

}
