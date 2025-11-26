package com.agile.controller;

import com.agile.constant.Constants;
import com.agile.dto.LeaveRequest;
import com.agile.dto.ResponseDto;
import com.agile.service.LeaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/leave")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @PostMapping("/apply-leave")
    public ResponseDto applyLeave(@RequestBody LeaveRequest request){
        ResponseDto response= new ResponseDto();
        try{
            log.info("request recieved for /apply-leave: {}", request);
            response= leaveService.applyLeave(request);
        }catch(Exception e){
            log.error("Error in applyLeave()",e);
            e.printStackTrace();
            response = new ResponseDto();
            response.setStatusCode(Constants.FAILED_CODE);
            response.setMessage(e.getMessage());

        }
        log.info("response sent for /apply-leave: {}", response);
        return response;
    }
    @PutMapping("/approve")
    public ResponseDto approveLeave(@RequestParam Long leaveId, @RequestParam String action){
        ResponseDto response= new ResponseDto();
        try{
            log.info("request received for /approve-leave: {}",leaveId);
            response= leaveService.approveLeave(leaveId, action);
        }catch(Exception e){
            log.error("Error in approveLeave()",e);
            e.printStackTrace();
            response = new ResponseDto();
            response.setStatusCode(Constants.FAILED_CODE);
            response.setMessage(e.getMessage());
        }
        log.info("response sent for /approve-leave: {}", response);
        return response;
    }

    @GetMapping("/get-leaves-by-admin")
    public ResponseDto getLeavesByAdmin(){
        ResponseDto response= new ResponseDto();

        try{
            log.info("request received for /get-leaves-by-admin: {}");
            response= leaveService.getLeavesByAdmin();
        }catch(Exception e){
            log.error("Error in getLeavesByAdmin()",e);
            e.printStackTrace();
            response = new ResponseDto();
            response.setStatusCode(Constants.FAILED_CODE);
            response.setMessage(e.getMessage());
        }
        log.info("response sent for /get-leaves-by-admin: {}", response);
        return response;
    }

    @GetMapping("/get-leaves-by-user")
    public ResponseDto getLeavesByUserId(){
        ResponseDto response= new ResponseDto();

        try{
            log.info("request received for /get-leaves-by-userid: {}");

            response= leaveService.getLeavesByUserId();
        }catch(Exception e){
            log.error("Error in getLeavesByUserId()",e);
            e.printStackTrace();
            response = new ResponseDto();
            response.setStatusCode(Constants.FAILED_CODE);
            response.setMessage(e.getMessage());
        }
        log.info("response sent for /get-leaves-by-userid: {}", response);
        return response;
    }
}
