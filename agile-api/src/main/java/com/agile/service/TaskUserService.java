package com.agile.service;

import com.agile.Status;
import com.agile.UserType;
import com.agile.constant.Constants;
import com.agile.dto.ResponseDto;
import com.agile.dto.TaskResponseDto;
import com.agile.dto.TaskUserDto;
import com.agile.model.*;
import com.agile.repository.*;
import com.agile.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.agile.UserType.ADMIN;

@Service
public class TaskUserService {
    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskUserRepository taskUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;



    public ResponseDto getTasks(Long sprintId) {
        ResponseDto responseDto= new ResponseDto();
        Utility utility= new Utility();
        String userName= utility.getUserName();
        User user= userRepository.findByEmail(userName);
        Sprint sprint= sprintRepository.findById(sprintId).orElse(null);

        if(user.getType().equals(ADMIN)){
            List<Task> tasks= taskRepository.getAllFromSprintId(sprintId);
            List<TaskResponseDto> taskResponseDtoList= new ArrayList<>();
            for(Task t: tasks){
                double used=0;
                Status status= Status.COMPLETED;
                List<Comment> commentList= commentRepository.getByTaskId(t.getId());
                for(Comment c: commentList){
                    used+=c.getUsed();
                    if(!c.getStatus().equals(Status.COMPLETED)){
                        status= Status.IN_PROGRESS;
                    }
                }
                TaskResponseDto taskResponseDto= new TaskResponseDto();
                taskResponseDto.setOwner(t.getOwner());
                taskResponseDto.setId(t.getId());
                taskResponseDto.setCreatedAt(t.getCreatedAt());
                taskResponseDto.setStatus(status);
                taskResponseDto.setPriority(t.getPriority());
                taskResponseDto.setProject(t.getProject());
                taskResponseDto.setTitle(t.getTitle());
                taskResponseDto.setEstimated(t.getEstimated());
                taskResponseDto.setSprint(t.getSprint());
                taskResponseDto.setRunningTime(t.getRunningTime());
                taskResponseDto.setTotalUsed(used);
                taskResponseDto.setUpdatedAt(t.getUpdatedAt());
                taskResponseDto.setUpdatedBy(t.getUpdatedBy());
                taskResponseDtoList.add(taskResponseDto);
            }
            responseDto.setData(taskResponseDtoList);
            responseDto.setMessage("tasks");
            return responseDto;
        }
        List<TaskUser> taskUserList= taskUserRepository.findByUserId(user.getId());
        List<Long> taskIdList= new ArrayList<>();
        for(TaskUser tu: taskUserList){
            taskIdList.add(tu.getId().getTaskId());
        }
        if(taskIdList.size()>0){
            List<Task> taskList= taskRepository.getAllFromTaskIds(taskIdList);
            List<Task> taskList1= new ArrayList<>();
            for(Task t: taskList){
                if(Objects.equals(t.getSprint().getId(), sprintId)){
                    taskList1.add(t);
                }
            }
            List<TaskResponseDto> taskResponseDtoList= new ArrayList<>();
            for(Task t: taskList1){
                double used=0;
                Status status= Status.COMPLETED;
                List<Comment> commentList= commentRepository.getByTaskId(t.getId());
                for(Comment c: commentList){
                    used+=c.getUsed();
                    if(!c.getStatus().equals(Status.COMPLETED) ){
                        status= Status.IN_PROGRESS;
                    }
                }
                TaskResponseDto taskResponseDto= new TaskResponseDto();
                taskResponseDto.setOwner(t.getOwner());
                taskResponseDto.setId(t.getId());
                taskResponseDto.setCreatedAt(t.getCreatedAt());
                taskResponseDto.setStatus(status);
                taskResponseDto.setPriority(t.getPriority());
                taskResponseDto.setProject(t.getProject());
                taskResponseDto.setTitle(t.getTitle());
                taskResponseDto.setEstimated(t.getEstimated());
                taskResponseDto.setSprint(t.getSprint());
                taskResponseDto.setRunningTime(t.getRunningTime());
                taskResponseDto.setTotalUsed(used);
                taskResponseDto.setUpdatedAt(t.getUpdatedAt());
                taskResponseDto.setUpdatedBy(t.getUpdatedBy());
                taskResponseDtoList.add(taskResponseDto);
            }
            responseDto.setData(taskResponseDtoList);
            responseDto.setMessage("Tasks");
            return responseDto;
        }
        responseDto.setStatusCode(Constants.FAILED_CODE);
        responseDto.setMessage("No tasks for User");
        responseDto.setStatus(Constants.FAILED);
        return responseDto;
    }
}
