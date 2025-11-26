package com.agile.service;

import com.agile.UserType;
import com.agile.constant.Constants;
import com.agile.dto.*;
import com.agile.model.*;
import com.agile.repository.*;
import com.agile.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskUserRepository taskUserRepository;

    public ResponseDto addComment(CommentRequestDto commentRequestDto) {
        ResponseDto responseDto = new ResponseDto();
        Utility utility = new Utility();
        String userName = utility.getUserName();
        Task task = taskRepository.findById(commentRequestDto.getTaskId()).orElse(null);
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userName));

        Comment comment = new Comment();
        comment.setUsed(commentRequestDto.getUsed());
        comment.setCommentType(commentRequestDto.getCommentType());
        comment.setStatus(commentRequestDto.getStatus());
        comment.setDescription(commentRequestDto.getDescription());
        comment.setTask(task);
        comment.setCreatedBy(user.get().getUsername());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAttachedFile(commentRequestDto.getAttachedFile());
        comment.setAttachedImage(commentRequestDto.getAttachedImage());
        Comment savedComment = commentRepository.save(comment);
        Timesheet timesheet = new Timesheet();
        timesheet.setOwner(savedComment.getTask().getOwner());
        timesheet.setUsed(savedComment.getUsed());
        timesheet.setCommentDescription(savedComment.getDescription());
        timesheet.setCreatedAt(savedComment.getCreatedAt());
        timesheet.setTaskId(savedComment.getTask().getId());
        timesheet.setTaskDescription(savedComment.getTask().getTitle());
        timesheetRepository.save(timesheet);
        responseDto.setData(savedComment);
        responseDto.setMessage("Comments");
        return responseDto;
    }

    public ResponseDto getComments(Long taskId) {
        ResponseDto responseDto = new ResponseDto();
        List<Comment> commentList = commentRepository.getByTaskId(taskId);
        responseDto.setMessage("Comments");
        responseDto.setData(commentList);
        return responseDto;
    }

    public ResponseDto getTimesheet(TimesheetResponse request) {
        ResponseDto responseDto = new ResponseDto();
        Utility utility = new Utility();
        String userName = utility.getUserName();
        Project project = projectRepository.findById(request.getProjectId()).orElse(null);
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userName));

        List<Long> taskIdList = new ArrayList<>();
        List<Task> taskList = taskRepository.findByProjectId(request.getProjectId());
        if (user.get().getType().equals(UserType.ADMIN)) {
            for (Task t : taskList) {
                taskIdList.add(t.getId());
            }
        } else {
            for (Task t : taskList) {
                if (t.getOwner().equals(user.get().getUsername())) {
                    taskIdList.add(t.getId());
                }
            }
        }
        if (taskIdList.isEmpty()) {
            responseDto.setMessage("No Data Found!");
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setStatus(Constants.FAILED);
            return responseDto;
        }
        Pageable pageable= PageRequest.of(request.getPage(),request.getSize());
            Page<Timesheet> timesheetList = timesheetRepository.findByTaskIds(taskIdList,pageable);
        List<TimesheetResponseDto> timesheetResponseDtoList = new ArrayList<>();
        for (Timesheet t : timesheetList) {
            TimesheetResponseDto timesheetResponseDto = new TimesheetResponseDto();
            timesheetResponseDto.setProjectDescription(project.getName());
            timesheetResponseDto.setOwner(t.getOwner());
            timesheetResponseDto.setCommentDescription(t.getCommentDescription());
            timesheetResponseDto.setTaskId(t.getTaskId());
            timesheetResponseDto.setUsed(t.getUsed());
            timesheetResponseDto.setTaskDescription(t.getTaskDescription());
            timesheetResponseDto.setCreatedAt(t.getCreatedAt());
            timesheetResponseDto.setId(t.getId());
            timesheetResponseDtoList.add(timesheetResponseDto);
        }
        responseDto.setMessage("Timesheet");
        responseDto.setData(timesheetResponseDtoList);
        return responseDto;
    }

    public ResponseDto getAllTimesheet(TimesheetResponse<Timesheet> request) {
        ResponseDto responseDto = new ResponseDto();
        if (request.getStartDate() == null) {
            LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
            request.setStartDate(LocalDateTime.of(firstDayOfMonth, LocalTime.MIN));
        }
        if(request.getEndDate()==null){
            request.setEndDate(LocalDateTime.now());
        }
        Pageable pageable= PageRequest.of(request.getPage(),request.getSize());
        if(request.getUserName()==null || request.getUserName().isEmpty()){
            Page<Timesheet> timesheetList= timesheetRepository.findByCreatedDateBetween(request.getStartDate(),request.getEndDate(),pageable);
            List<TimesheetResponseDto> timesheetResponseDtoList = new ArrayList<>();
            for (Timesheet t : timesheetList.getContent()){
                Task task = taskRepository.findById(t.getTaskId()).orElse(null);
                if (task == null) continue;
                Project project = projectRepository.findById(task.getProject().getId()).orElse(null);
                TimesheetResponseDto timesheetResponseDto = new TimesheetResponseDto();
                timesheetResponseDto.setId(t.getId());
                timesheetResponseDto.setOwner(t.getOwner());
                timesheetResponseDto.setUsed(t.getUsed());
                timesheetResponseDto.setCommentDescription(t.getCommentDescription());
                timesheetResponseDto.setTaskId(t.getTaskId());
                timesheetResponseDto.setTaskDescription(t.getTaskDescription());
                timesheetResponseDto.setProjectDescription(project.getName());
                timesheetResponseDto.setCreatedAt(t.getCreatedAt());
                timesheetResponseDtoList.add(timesheetResponseDto);
            }
            responseDto.setMessage("Timesheet");
            responseDto.setData(timesheetResponseDtoList);
        }else{
            Page<Timesheet> timesheetList= timesheetRepository.findByCreatedBetweenAndUserName(request.getStartDate(),request.getEndDate(), request.getUserName(),pageable);
            List<TimesheetResponseDto> timesheetResponseDtoList = new ArrayList<>();
            for (Timesheet t : timesheetList.getContent()) {
                Task task = taskRepository.findById(t.getTaskId()).orElse(null);
                if (task == null) continue;
                Project project = projectRepository.findById(task.getProject().getId()).orElse(null);
                TimesheetResponseDto timesheetResponseDto = new TimesheetResponseDto();
                timesheetResponseDto.setId(t.getId());
                timesheetResponseDto.setOwner(t.getOwner());
                timesheetResponseDto.setUsed(t.getUsed());
                timesheetResponseDto.setCommentDescription(t.getCommentDescription());
                timesheetResponseDto.setTaskId(t.getTaskId());
                timesheetResponseDto.setTaskDescription(t.getTaskDescription());
                timesheetResponseDto.setProjectDescription(project.getName());
                timesheetResponseDto.setCreatedAt(t.getCreatedAt());
                timesheetResponseDtoList.add(timesheetResponseDto);
            }
            responseDto.setMessage("Timesheet");
            responseDto.setData(timesheetResponseDtoList);
        }
        return responseDto;
    }

    public ResponseDto deleteComment(Long commentId) {
        ResponseDto responseDto = new ResponseDto();
        Utility utility = new Utility();
        String userName = utility.getUserName();
        User user = userRepository.findByEmail(userName);
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment.getCreatedBy().equals(user.getUsername())) {
            String commentDescription = comment.getDescription();
            Timesheet timesheet = timesheetRepository.findByCommentDesc(commentDescription).orElse(null);
            if (timesheet != null) {
                timesheetRepository.delete(timesheet);
            }
            commentRepository.delete(comment);


            responseDto.setData(comment);
            responseDto.setMessage("Comment successfully deleted");
            return responseDto;
        }
        responseDto.setMessage("Not allowed!");
        responseDto.setData(null);
        responseDto.setStatusCode(Constants.FAILED_CODE);
        responseDto.setStatus(Constants.FAILED);
        return responseDto;
    }

    public ResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        ResponseDto responseDto = new ResponseDto();
        Utility utility = new Utility();
        String userName = utility.getUserName();
        User user = userRepository.findByEmail(userName);
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (user.getUsername().equals(comment.getCreatedBy())) {
            comment.setStatus(commentRequestDto.getStatus());
            comment.setCommentType(commentRequestDto.getCommentType());
            comment.setDescription(commentRequestDto.getDescription());
            comment.setUsed(commentRequestDto.getUsed());
            comment.setAttachedImage(commentRequestDto.getAttachedImage());
            comment.setAttachedImage(commentRequestDto.getAttachedImage());
            Comment saved = commentRepository.save(comment);
            Timesheet timesheet = timesheetRepository.findByCommentDesc(comment.getDescription()).orElse(null);
            timesheet.setOwner(saved.getTask().getOwner());
            timesheet.setUsed(saved.getUsed());
            timesheet.setCommentDescription(saved.getDescription());
            timesheet.setCreatedAt(saved.getCreatedAt());
            timesheet.setTaskId(saved.getTask().getId());
            timesheet.setTaskDescription(saved.getTask().getTitle());
            timesheetRepository.save(timesheet);
            responseDto.setData(saved);
            responseDto.setMessage("Successfully Updated");
            return responseDto;
        }
        responseDto.setData(null);
        responseDto.setMessage("Not Allowed!");
        responseDto.setStatus(Constants.FAILED);
        responseDto.setStatusCode(Constants.FAILED_CODE);
        return responseDto;
    }

//    public ResponseDto getTimesheetByDate(TimesheetWithFilterDto filterDto) {
//        ResponseDto responseDto = new ResponseDto();
//        Utility utility = new Utility();
//        String userName = utility.getUserName();
//        User user = userRepository.findByEmail(userName);
//        List<Timesheet> timesheetList = new ArrayList<>();
//        if (filterDto.getProjectId() == null) {
//            if (filterDto.getEndDate() == null) filterDto.setEndDate(LocalDateTime.now());
//            if (filterDto.getStartDate() == null) {
//                LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
//                filterDto.setStartDate(LocalDateTime.of(firstDayOfMonth, LocalTime.MIN));
//            }
//            if (filterDto.getUserName() == null || filterDto.getUserName().isEmpty()) {
//                if(user.getType().equals(UserType.ADMIN)){
//                    timesheetList = timesheetRepository.findByCreatedDateBetween(filterDto.getStartDate(), filterDto.getEndDate());
//                    responseDto.setData(timesheetList);
//                }
//                timesheetList = timesheetRepository.findByCreatedBetweenAndUserName(filterDto.getStartDate(), filterDto.getEndDate(), userName);
//               responseDto.setData(timesheetList);
//            } else {
//                timesheetList = timesheetRepository.findByCreatedBetweenAndUserName(filterDto.getStartDate(), filterDto.getEndDate(), filterDto.getUserName());
//                responseDto.setData(timesheetList);
//            }
//        } else {
//            Project project = projectRepository.findById(filterDto.getProjectId()).orElse(null);
//            List<Long> taskIdList = new ArrayList<>();
//            if (user.getType().equals(UserType.ADMIN)) {
//                List<Task> taskList = taskRepository.findByProjectId(project.getId());
//                for (Task t : taskList) {
//                    taskIdList.add(t.getId());
//                }
//            } else {
//                List<TaskUser> taskUser = taskUserRepository.findByUserId(user.getId());
//                for (TaskUser t : taskUser) {
//                    taskIdList.add(t.getId().getTaskId());
//                }
//            }
//            if (taskIdList.isEmpty()) {
//                responseDto.setMessage("No Data Found!");
//                responseDto.setStatusCode(Constants.FAILED_CODE);
//                responseDto.setStatus(Constants.FAILED);
//                return responseDto;
//            }
////        List<Timesheet> timesheetList = new ArrayList<>();
//            if (filterDto.getStartDate() == null || filterDto.getEndDate() == null) {
//                timesheetList = timesheetRepository.findByTaskIds(taskIdList);
//            } else {
//                timesheetList = timesheetRepository.findTimesheetsByStartDateBetweenAndTaskIds(filterDto.getStartDate(),
//                    filterDto.getEndDate(), taskIdList);
//            }
//            List<TimesheetResponseDto> timesheetResponseDtoList = new ArrayList<>();
//            for (Timesheet t : timesheetList) {
//                TimesheetResponseDto timesheetResponseDto = new TimesheetResponseDto();
//                timesheetResponseDto.setProjectDescription(project.getName());
//                timesheetResponseDto.setOwner(t.getOwner());
//                timesheetResponseDto.setCommentDescription(t.getCommentDescription());
//                timesheetResponseDto.setTaskId(t.getTaskId());
//                timesheetResponseDto.setUsed(t.getUsed());
//                timesheetResponseDto.setTaskDescription(t.getTaskDescription());
//                timesheetResponseDto.setCreatedAt(t.getCreatedAt());
//                timesheetResponseDto.setId(t.getId());
//                timesheetResponseDtoList.add(timesheetResponseDto);
//            }
//            responseDto.setMessage("Timesheet");
//            responseDto.setData(timesheetResponseDtoList);
//           return responseDto;
//        }
//        return responseDto;
//    }
}
