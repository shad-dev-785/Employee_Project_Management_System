package com.agile.service;

import com.agile.UserType;
import com.agile.constant.Constants;
import com.agile.constant.LeaveStatus;
import com.agile.dto.LeaveRequest;
import com.agile.dto.ResponseDto;
import com.agile.model.Leave;
import com.agile.model.Project;
import com.agile.model.User;
import com.agile.model.UserLeave;
import com.agile.repository.*;
import com.agile.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LeaveService {
@Autowired
private UserRepository userRepository;
    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private UserLeaveRepository userLeaveRepository;
    @Autowired
    private ProjectUserRepository projectUserRepository;
    @Autowired
    private ProjectUserService projectUserService;



    public ResponseDto applyLeave(LeaveRequest request) {
        ResponseDto responseDto= new ResponseDto();
        Utility utility= new Utility();
        String userName= utility.getUserName();
        User user= userRepository.findByEmail(userName);
        boolean areAdminAndUserInSameProject= false;

        User user1= userRepository.getUserByUserName(request.getApprovedBy()).orElse(null);
        ResponseDto response= projectUserService.getProjectFromUserId(user.getId());
        List<Project> userProjectList= (List<Project>) response.getData();
        Set<Project> userProjectSet= new HashSet<>(userProjectList);
        ResponseDto response1= projectUserService.getProjectFromUserId(user1.getId());
        List<Project> adminProjectList= (List<Project>) response.getData();
//        to confirm that both the user who is requesting leave and
//        the user who is approving shall be added in same project.
        for(Project p: adminProjectList){
            if(userProjectSet.contains(p)){
                areAdminAndUserInSameProject= true;
                break;
            }
        }
        if(user1.getType().equals(UserType.ADMIN) && areAdminAndUserInSameProject){
            Leave leave= new Leave();
            leave.setLeaveType(request.getLeaveType());
            UserLeave userLeave= userLeaveRepository.findByuserId(user.getId()).orElse(null);
            switch (leave.getLeaveType()) {
                case EL:
                    if(userLeave.getEl()<request.getDays()){
                        responseDto.setStatus(Constants.FAILED);
                        responseDto.setStatusCode(Constants.FAILED_CODE);
                        responseDto.setMessage("You don't have enough ELs remaining.Kindly contact Manager!");
                        return responseDto;
                    }
                    break;
                case CL:
                    if(userLeave.getCl()<request.getDays()){
                        responseDto.setStatus(Constants.FAILED);
                        responseDto.setStatusCode(Constants.FAILED_CODE);
                        responseDto.setMessage("You don't have enough CLs remaining.Kindly contact Manager!");
                        return responseDto;
                    }
                    break;
                case PL:
                    if(userLeave.getPl()<request.getDays()){
                        responseDto.setStatus(Constants.FAILED);
                        responseDto.setStatusCode(Constants.FAILED_CODE);
                        responseDto.setMessage("You don't have enough PLs remaining.Kindly contact Manager!");
                        return responseDto;
                    }
                    break;
                case SL:
                    if(userLeave.getSl()<request.getDays()){
                        responseDto.setStatus(Constants.FAILED);
                        responseDto.setStatusCode(Constants.FAILED_CODE);
                        responseDto.setMessage("You don't have enough SLs remaining.Kindly contact Manager!");
                        return responseDto;
                    }
                    break;
            }
            leave.setDays(request.getDays());
            leave.setApprovedBy(user1.getUsername());
            leave.setStatus(LeaveStatus.PENDING);
            leave.setStartDate(request.getStartDate());
            leave.setEndDate(request.getEndDate());
            leave.setCreatedAt(LocalDateTime.now());
            leave.setUpdatedAt(LocalDateTime.now());
            leave.setUser(user);
            Leave savedLeave= leaveRepository.save(leave);
            responseDto.setMessage("Leaved Applied Successfully");
            responseDto.setData(savedLeave);
            return responseDto;
        }
        responseDto.setStatus(Constants.FAILED);
        responseDto.setStatusCode(Constants.FAILED_CODE);
        responseDto.setMessage("Leave approval not requested to your project Admin!");
        return responseDto;



    }
    public ResponseDto approveLeave(Long leaveId, String action){
        ResponseDto responseDto= new ResponseDto();
        Leave leave = leaveRepository.findById(leaveId).orElse(null);

        // Update user leave balance
        UserLeave userLeave = userLeaveRepository.findByuserId(leave.getUser().getId()).orElse(null);

        switch (leave.getLeaveType()) {
            case EL:
                if(action.equals("APPROVED")){
                    leave.setStatus(LeaveStatus.APPROVED);
                    userLeave.setEl(userLeave.getEl() - leave.getDays());
                }else{
                    leave.setStatus(LeaveStatus.REJECTED);
                }

                break;
            case CL:
                if(action.equals("APPROVED")){
                    leave.setStatus(LeaveStatus.APPROVED);
                    userLeave.setCl(userLeave.getCl() - leave.getDays());
                }else{
                    leave.setStatus(LeaveStatus.REJECTED);
                }
                break;
            case PL:
                if(action.equals("APPROVED")){
                    leave.setStatus(LeaveStatus.APPROVED);
                    userLeave.setPl(userLeave.getPl() - leave.getDays());
                }else{
                    leave.setStatus(LeaveStatus.REJECTED);
                }
                break;
            case SL:
                if(action.equals("APPROVED")){
                    leave.setStatus(LeaveStatus.APPROVED);
                    userLeave.setSl(userLeave.getSl() - leave.getDays());
                }else{
                    leave.setStatus(LeaveStatus.REJECTED);
                }
                break;
        }

        userLeaveRepository.save(userLeave);

        // Update leave status to approved


        leaveRepository.save(leave);
        responseDto.setMessage("Action taken on leave Request!");
        return responseDto;

    }
    public ResponseDto getLeavesByAdmin(){
        Utility utility= new Utility();
        ResponseDto responseDto= new ResponseDto();
        String userName= utility.getUserName();
        User user= userRepository.findByEmail(userName);
        if(user.getType().equals(UserType.ADMIN)){
            List<Leave> leaves= leaveRepository.findAllByApprovedBy(user.getUsername());
            responseDto.setMessage("Leaves to be Approved!");
            responseDto.setData(leaves);
            return responseDto;
        }
        responseDto.setStatus(Constants.FAILED);
        responseDto.setStatusCode(Constants.FAILED_CODE);
        responseDto.setMessage("Not Allowed!");
        return responseDto;
    }

    public ResponseDto getLeavesByUserId() {
        Utility utility= new Utility();
        ResponseDto responseDto= new ResponseDto();
        String userName= utility.getUserName();
        User user= userRepository.findByEmail(userName);
        List<Leave> leaves= leaveRepository.finalAllByUerId(user.getId());
        responseDto.setMessage("Leaves Requested!");
        responseDto.setData(leaves);
        return responseDto;
    }
}
