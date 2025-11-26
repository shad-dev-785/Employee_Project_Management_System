package com.agile.controller;

import com.agile.constant.Constants;
import com.agile.dto.PaginationResponse;
import com.agile.dto.ResponseDto;
import com.agile.model.Attendance;
import com.agile.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;
    @PostMapping("/check-in")
    public ResponseDto checkIn(){
        return attendanceService.checkIn();
    }
    @PutMapping("/check-out")
    public ResponseDto checkOut(){
        return attendanceService.checkOut();
    }
    @PostMapping("/get-attendance-by-userid")
    public ResponseDto getAttendanceByUserId(@RequestBody PaginationResponse<Attendance> request){
        ResponseDto responseDto= new ResponseDto();
        try{
            log.info("request received for /get-attendance-by-userid: {}",request);
            responseDto= attendanceService.getAttendanceByUserId(request);
        }catch (Exception e){
            log.error("Error in getAttendanceByUserId()",e);
            e.printStackTrace();
            responseDto = new ResponseDto();
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setMessage(e.getMessage());
        }
        log.info("response sent for /get-attendance-by-userid: {}");
        return responseDto;
    }
    @PostMapping("/get-attendance-by-admin")
    public ResponseDto getAttendanceByAdmin(@RequestBody PaginationResponse<Attendance> request){
        ResponseDto responseDto= new ResponseDto();
        try{
            log.info("request received for /get-attendance-by-admin: {}",request);
            responseDto= attendanceService.getAttendanceByAdmin(request);
        }catch (Exception e){
            log.error("Error in getAttendanceByAdmin()",e);
            e.printStackTrace();
            responseDto = new ResponseDto();
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setMessage(e.getMessage());
        }
        log.info("response sent for /get-attendance-by-admin: {}");
        return responseDto;
    }

}
