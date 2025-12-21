package com.agile.project_service.service;

import com.agile.dto.ResponseDto;
import com.agile.project_service.dto.AddUserToProjectDto;
import com.agile.project_service.dto.ProjectDto;
import com.agile.project_service.dto.ProjectResponseDto;
import com.agile.project_service.entity.Project;
import com.agile.project_service.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    ModelMapper modelMapper;

    public ResponseDto createProject(ProjectDto dto) {
        // Business logic to create a project
        ResponseDto responseDto= new ResponseDto();
        Project project= projectRepository.existsByName(dto.getName());
        if (project!=null){
            responseDto.setMessage("Project with the same name already exists");
            return responseDto;
        }
        Project project1= new Project();
        modelMapper.map(dto, project1);
        Project savedproject = projectRepository.save(project1);
        ProjectResponseDto projectResponseDto= modelMapper.map(savedproject, ProjectResponseDto.class);
        responseDto.setMessage("Project created successfully");
        responseDto.setData(projectResponseDto);
        return new ResponseDto();
    }

    public ResponseDto addMembersToProject(AddUserToProjectDto dto) {
    return new ResponseDto();
    }
}
