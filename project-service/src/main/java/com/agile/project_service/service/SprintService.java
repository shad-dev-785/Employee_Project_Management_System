package com.agile.project_service.service;

import com.agile.dto.ResponseDto;
import com.agile.project_service.config.IdentityClient;
import com.agile.project_service.dto.SprintDto;
import com.agile.project_service.entity.Sprint;
import com.agile.project_service.repository.SprintRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SprintService {
    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IdentityClient identityClient;

    public ResponseDto createSprint(SprintDto sprintDto) {
        ResponseDto responseDto = new ResponseDto();
        Boolean exists= sprintRepository.existsByName(sprintDto.getName());
        if (exists) {
            responseDto.setMessage("Sprint with the same name already exists");
            return responseDto;
        }
        Sprint sprint = modelMapper.map(sprintDto, Sprint.class);
        Sprint savedSprint = sprintRepository.save(sprint);
        SprintDto savedSprintDto = modelMapper.map(savedSprint, SprintDto.class);
        responseDto.setData(savedSprintDto);
        responseDto.setMessage("Sprint created successfully");
        return responseDto;
    }

    public ResponseDto assignMembersToSprint(SprintDto sprintDto) {
    return new ResponseDto();
    }
}
