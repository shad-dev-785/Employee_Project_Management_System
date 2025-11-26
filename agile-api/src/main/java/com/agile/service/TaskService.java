package com.agile.service;

import com.agile.Status;
import com.agile.UserType;
import com.agile.constant.Constants;
import com.agile.dto.ResponseDto;
import com.agile.dto.TaskRequestDto;
import com.agile.dto.TaskResponseDto;
import com.agile.dto.TaskWithCommentDto;
import com.agile.model.*;
import com.agile.repository.*;
import com.agile.utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SprintUserRepository sprintUserRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TaskUserRepository taskUserRepository;
    @Autowired
    private TimesheetRepository timesheetRepository;
    @Autowired
    JavaMailSender javaMailSender;

    public ResponseDto addTask(TaskWithCommentDto taskWithCommentDto) {
        ResponseDto responseDto= new ResponseDto();
        Utility utility= new Utility();
        String userName= utility.getUserName();
        Optional<User> user= Optional.ofNullable(userRepository.findByEmail(userName));
        User user1= userRepository.getUserByUserName(taskWithCommentDto.getTaskRequestDto().getOwner()).orElse(null);
        Sprint sprint= sprintRepository.findById(taskWithCommentDto.getTaskRequestDto().getSprintId()).orElse(null);
        if(sprint==null){
            responseDto.setData(null);
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setStatus(Constants.FAILED);
            responseDto.setMessage("Sprint not Present!");
            return responseDto;
        }
        Task task1= new Task();
        task1.setCreatedBy(user.get().getUsername());
        task1.setPriority(taskWithCommentDto.getTaskRequestDto().getPriority());
        task1.setCreatedAt(taskWithCommentDto.getTaskRequestDto().getCreatedAt());
        task1.setOwner(taskWithCommentDto.getTaskRequestDto().getOwner());
        task1.setEstimated(taskWithCommentDto.getTaskRequestDto().getEstimated());
        task1.setSprint(sprint);
        task1.setProject(sprint.getProject());
        task1.setRunningTime(taskWithCommentDto.getTaskRequestDto().getRunningTime());
        task1.setTitle(taskWithCommentDto.getTaskRequestDto().getTitle());
        if(user.get().getType().equals(UserType.ADMIN) || task1.getOwner().equals(user.get().getUsername())){
            Comment comment= new Comment();
            comment.setTask(task1);
            comment.setStatus(taskWithCommentDto.getCommentRequestDto().getStatus());
            comment.setCommentType(taskWithCommentDto.getCommentRequestDto().getCommentType());
//            comment.setOwner(user.get().getUsername());
            comment.setAttachedFile(taskWithCommentDto.getCommentRequestDto().getAttachedFile());
            comment.setAttachedImage(taskWithCommentDto.getCommentRequestDto().getAttachedImage());
            comment.setCreatedAt(taskWithCommentDto.getTaskRequestDto().getCreatedAt());
            comment.setCreatedBy(user.get().getUsername());
            comment.setDescription(taskWithCommentDto.getCommentRequestDto().getDescription());
            comment.setUsed(taskWithCommentDto.getCommentRequestDto().getUsed());

            Task savedTask = taskRepository.save(task1);
            Comment savedComment=commentRepository.save(comment);
            TaskResponseDto taskResponseDto= new TaskResponseDto();
            //         for creating response as there is no used and status in task entity!
            taskResponseDto.setId(savedTask.getId());
            taskResponseDto.setSprint(savedTask.getSprint());
            taskResponseDto.setTitle(savedTask.getTitle());
            taskResponseDto.setOwner(savedTask.getOwner());
            taskResponseDto.setEstimated(savedTask.getEstimated());
            taskResponseDto.setProject(savedTask.getProject());
            taskResponseDto.setPriority(savedTask.getPriority());
            taskResponseDto.setCreatedAt(savedTask.getCreatedAt());
            taskResponseDto.setRunningTime(savedTask.getRunningTime());
            taskResponseDto.setCreatedBy(savedTask.getCreatedBy());
            taskResponseDto.setUpdatedAt(savedTask.getUpdatedAt());
            taskResponseDto.setUpdatedBy(savedTask.getUpdatedBy());
            double used=0;
            Status status= Status.COMPLETED;
            List<Comment> comments= commentRepository.getByTaskId(savedTask.getId());
            for(Comment c: comments){
                used+=c.getUsed();
                if(!c.getStatus().equals(Status.COMPLETED)){
                    status= Status.IN_PROGRESS;
                }
            }
            taskResponseDto.setStatus(status);
            taskResponseDto.setTotalUsed(used);


            sendEmail(savedTask);
            Timesheet timesheet= new Timesheet();
            timesheet.setTaskDescription(savedTask.getTitle());
            timesheet.setTaskId(savedTask.getId());
            timesheet.setCommentDescription(savedComment.getDescription());
            timesheet.setOwner(savedTask.getOwner());
            timesheet.setUsed(savedComment.getUsed());
            timesheet.setCreatedAt(savedTask.getCreatedAt());
            timesheetRepository.save(timesheet);

            TaskUser taskUser= new TaskUser();
            TaskUserId taskUserId= new TaskUserId();
            taskUserId.setTaskId(savedTask.getId());
            TaskUser taskUser1= taskUserRepository.findByTaskAndUserId(savedTask.getId(), user1.getId()).orElse(null);
            if(taskUser1!=null){
                responseDto.setMessage("Failed to create task!");
                responseDto.setStatus(Constants.FAILED);
                responseDto.setStatusCode(Constants.FAILED_CODE);
                return responseDto;
            }
            taskUserId.setUserId(user1.getId());
            taskUser.setId(taskUserId);
            taskUserRepository.save(taskUser);
            responseDto.setData(taskResponseDto);
            responseDto.setMessage("Task Created Successfully");
            return responseDto;
        }
        responseDto.setMessage("Not Allowed!");
        responseDto.setStatus(Constants.FAILED);
        responseDto.setStatusCode(Constants.FAILED_CODE);
        return responseDto;
    }

    public ResponseDto updateTask(Long taskId, TaskRequestDto taskRequestDto) {
        ResponseDto responseDto= new ResponseDto();
        Utility utility= new Utility();
        String userName= utility.getUserName();
        User user= userRepository.findByEmail(userName);
        Task task= taskRepository.findById(taskId).orElse(null);
        User user1= userRepository.getUserByUserName(taskRequestDto.getOwner()).orElse(null);
        Sprint sprint= sprintRepository.findById(task.getSprint().getId()).orElse(null);
        if (task==null){
            responseDto.setData(null);
            responseDto.setMessage("Task not found!");
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setStatus(Constants.FAILED);
            return responseDto;
        }
        if(sprint==null){
            responseDto.setData(null);
            responseDto.setMessage("Sprint not found!");
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setStatus(Constants.FAILED);
            return responseDto;
        }
        task.setTitle(taskRequestDto.getTitle());
        task.setOwner(taskRequestDto.getOwner());
        task.setSprint(sprint);
        task.setEstimated(taskRequestDto.getEstimated());
        task.setPriority(taskRequestDto.getPriority());
        task.setRunningTime(taskRequestDto.getRunningTime());
        task.setUpdatedAt(LocalDateTime.now());
        task.setUpdatedBy(user.getUsername());
        if(user.getType().equals(UserType.ADMIN) || task.getOwner().equals(user.getUsername())){
            Task savedTask= taskRepository.save(task);
            TaskUser taskUser= taskUserRepository.findByTaskId(savedTask.getId()).orElse(null);
            TaskUserId taskUserId= new TaskUserId();
            taskUserId.setUserId(user1.getId());
            taskUserId.setTaskId(savedTask.getId());
            taskUser.setId(taskUserId);
            taskUserRepository.save(taskUser);
//         for creating response as there is no used and status in task entity!
            TaskResponseDto taskResponseDto= new TaskResponseDto();
            taskResponseDto.setId(savedTask.getId());
            taskResponseDto.setSprint(savedTask.getSprint());
            taskResponseDto.setTitle(savedTask.getTitle());
            taskResponseDto.setOwner(savedTask.getOwner());
            taskResponseDto.setEstimated(savedTask.getEstimated());
            taskResponseDto.setProject(savedTask.getProject());
            taskResponseDto.setPriority(savedTask.getPriority());
            taskResponseDto.setCreatedAt(savedTask.getCreatedAt());
            taskResponseDto.setRunningTime(savedTask.getRunningTime());
            taskResponseDto.setCreatedBy(savedTask.getCreatedBy());
            taskResponseDto.setUpdatedAt(savedTask.getUpdatedAt());
            taskResponseDto.setUpdatedBy(savedTask.getUpdatedBy());
            double used=0;
            Status status= Status.COMPLETED;
            List<Comment> comments= commentRepository.getByTaskId(savedTask.getId());
            for(Comment c: comments){
                used+=c.getUsed();
                if(!c.getStatus().equals(Status.COMPLETED)){
                    status= Status.IN_PROGRESS;
                }
            }
            taskResponseDto.setStatus(status);
            taskResponseDto.setTotalUsed(used);
            responseDto.setData(taskResponseDto);
            responseDto.setMessage("Task successfully updated");
            return responseDto;
        }
        responseDto.setMessage("User doesn't have authority to modify tasks!");
        responseDto.setStatusCode(Constants.FAILED_CODE);
        responseDto.setStatus(Constants.FAILED);
        return responseDto;
    }

    public ResponseDto deleteTask(Long taskId) {
        ResponseDto responseDto= new ResponseDto();
        Utility utility= new Utility();
        String userName= utility.getUserName();
        User user= userRepository.findByEmail(userName);
        Task task= taskRepository.findById(taskId).orElse(null);
        TaskUser taskUser= taskUserRepository.findByTaskId(taskId).orElse(null);
        if(task!=null){
            if(user.getUsername().equals(task.getCreatedBy())){
                commentRepository.deleteAllByTaskId(taskId);
                taskRepository.delete(task);
                taskUserRepository.deleteTask(taskUser.getId().getTaskId());
//                Optional<TaskUser> taskuser = taskUserRepository.findByTaskId(taskId);
//                taskuser.ifPresent(ele -> taskUserRepository.delete(ele));

                List<Timesheet> timesheets= timesheetRepository.findAllByTaskId(task.getId());
                if(timesheets.size()>0) {
                    for (Timesheet t : timesheets) {
                        timesheetRepository.deleteById(t.getId());
                    }
                    responseDto.setMessage("Task deleted Successfully");
                    responseDto.setData(task);
                    return responseDto;
                }
            }
            responseDto.setMessage("Not Allowed!");
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setStatus(Constants.FAILED);
            return responseDto;
        }

        responseDto.setMessage("Task not found");
        responseDto.setStatusCode(Constants.FAILED_CODE);
        responseDto.setStatus(Constants.FAILED);
        return responseDto;
    }
    @Async
    public void sendEmail(Task task){
try{
    String text = "You have been assigned a task by "+ task.getCreatedBy() + " in the Project " + task.getProject().getName();
    User user= userRepository.getUserByUserName(task.getOwner()).orElse(null);

    SimpleMailMessage mail = new SimpleMailMessage();
    mail.setTo(user.getEmail());
    mail.setSubject("New Task Assigned!");
    mail.setText(text);
    javaMailSender.send(mail);
}catch(Exception e){
    e.printStackTrace();
    log.error("exception", e);
}
    }

    public ResponseDto getTaskById(Long id) {
        ResponseDto responseDto= new ResponseDto();
        Task task= taskRepository.findById(id).orElse(null);
        if(task == null){
            responseDto.setMessage("Task Doesn't Exists!");
            responseDto.setStatus(Constants.FAILED);
            responseDto.setStatusCode(Constants.FAILED_CODE);
        }else{
            TaskResponseDto taskResponseDto= new TaskResponseDto();
            double used=0;
            Status status= Status.COMPLETED;
            List<Comment> comments= commentRepository.getByTaskId(id);
            for(Comment c: comments){
                used+=c.getUsed();
                if(!c.getStatus().equals(Status.COMPLETED)){
                    status= Status.IN_PROGRESS;
                }
            }
            taskResponseDto.setProject(task.getProject());
            taskResponseDto.setSprint(task.getSprint());
            taskResponseDto.setId(task.getId());
            taskResponseDto.setStatus(status);
            taskResponseDto.setTotalUsed(used);
            taskResponseDto.setOwner(task.getOwner());
            taskResponseDto.setTitle(task.getTitle());
            taskResponseDto.setEstimated(task.getEstimated());
            taskResponseDto.setPriority(task.getPriority());
            taskResponseDto.setUpdatedAt(task.getUpdatedAt());
            taskResponseDto.setUpdatedBy(task.getUpdatedBy());
            taskResponseDto.setCreatedAt(task.getCreatedAt());
            taskResponseDto.setCreatedBy(task.getCreatedBy());
            taskResponseDto.setRunningTime(task.getRunningTime());
            responseDto.setData(taskResponseDto);
            responseDto.setMessage("Task");
        }
        return responseDto;
    }


}
