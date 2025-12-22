package com.agile.project_service.service;

import com.agile.dto.ResponseDto;
import com.agile.exception.EpmsException;
import com.agile.project_service.config.IdentityClient;
import com.agile.project_service.dto.AddUserToProjectDto;
import com.agile.project_service.dto.ProjectDto;
import com.agile.project_service.dto.ProjectResponseDto;
import com.agile.project_service.dto.UserObject;
import com.agile.project_service.entity.Project;
import com.agile.project_service.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    IdentityClient identityClient;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    ModelMapper modelMapper;

    public ResponseDto createProject(ProjectDto dto) {
        // Business logic to create a project
        ResponseDto responseDto= new ResponseDto();
        Boolean project= projectRepository.existsByName(dto.getName());
        if (project){
            responseDto.setMessage("Project with the same name already exists");
            return responseDto;
        }
        Project project1= new Project();
        modelMapper.map(dto, project1);
        Project savedproject = projectRepository.save(project1);
        ProjectResponseDto projectResponseDto= modelMapper.map(savedproject, ProjectResponseDto.class);
        responseDto.setMessage("Project created successfully");
        responseDto.setData(projectResponseDto);
        return responseDto;
    }
  @CacheEvict(value = "projects", key = "#projectId")
    public ResponseDto addMembersToProject(Long projectId, AddUserToProjectDto dto) {
        ResponseDto responseDto= new ResponseDto();
        try{
            for(Long userId : dto.getUserIds()){
                // Logic to add user to project
                ResponseDto user= identityClient.getUserById(userId);
                if(user.getData() != null){
                    UserObject userObject= modelMapper.map(user.getData(), UserObject.class);
                    Project project= projectRepository.findById(projectId).orElseThrow(()-> new EpmsException("Project not found with id: " + projectId));
                    project.getMemberIds().add(userObject.getId());
                 Project savedProject = projectRepository.save(project);
                    responseDto.setMessage("Users added to project successfully");
                    responseDto.setData(savedProject);
                } else {
                    throw new EpmsException("User not found with id: " + userId);
                }}

        }catch (Exception e){
            throw new EpmsException("Error adding users to project: " + e.getMessage());
        }
          return responseDto;
    }
    @CacheEvict(value = "projects", allEntries = true)
    public ResponseDto getAllProjects() {
        ResponseDto responseDto= new ResponseDto();
        List<Project> projects = projectRepository.findAll();
        responseDto.setMessage("Projects retrieved successfully");
        responseDto.setData(projects);
        return responseDto;
    }
    @CacheEvict(value = "projects", key = "#userId")
    public ResponseDto findProjectsByUserId(Long userId) {
        ResponseDto responseDto= new ResponseDto();
        List<Project> projects = projectRepository.findByMemberIdsContaining(userId);
        if (projects.isEmpty()) {
            responseDto.setMessage("No projects found for user id: " + userId);
            return responseDto;
        }
        responseDto.setMessage("Projects retrieved successfully for user id: " + userId);
        responseDto.setData(projects);
        return responseDto;
    }

@CacheEvict(value = "projects", key = "#projectId")
    public ResponseDto removeMemberFromProject(Long projectId, Long userId) {
        ResponseDto responseDto= new ResponseDto();
        Project project= projectRepository.findById(projectId).orElseThrow(()-> new EpmsException("Project not found with id: " + projectId));
        if(project.getMemberIds().contains(userId)){
            project.getMemberIds().remove(userId);
            Project savedProject = projectRepository.save(project);
            responseDto.setMessage("User removed from project successfully");
            responseDto.setData(savedProject);
        } else {
            throw new EpmsException("User with id: " + userId + " is not a member of project with id: " + projectId);
        }
        return responseDto;
    }
}
