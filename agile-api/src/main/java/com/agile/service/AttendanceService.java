package com.agile.service;

import com.agile.constant.AttendanceStatus;
import com.agile.constant.Constants;
import com.agile.dto.PaginationResponse;
import com.agile.dto.ResponseDto;
import com.agile.model.Attendance;
import com.agile.model.User;
import com.agile.repository.AttendanceRepository;
import com.agile.repository.UserRepository;
import com.agile.utility.Utility;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseDto checkIn(){
        ResponseDto responseDto= new ResponseDto();
        Utility utility=new Utility();
        String userName= utility.getUserName();
        User user= userRepository.findByEmail(userName);
        Attendance attendance1= attendanceRepository.findByUserIdAndDate(user.getId(),LocalDate.now()).orElse(null);
        if(attendance1==null){
            Attendance attendance= new Attendance();
            LocalTime current= LocalTime.now();
            LocalTime checkInTime= current.plusHours(5).plusMinutes(30);
            attendance.setCheckIn(checkInTime);
            if(attendance.getCheckIn().isAfter(LocalTime.of(9,30,00))){
                attendance.setStatus(AttendanceStatus.LATE_CHECK_IN);
            }else if(attendance.getCheckIn().isBefore(LocalTime.of(9,30,00))){
                attendance.setStatus(AttendanceStatus.PRESENT);
            }
            attendance.setName(user.getFirstname());
            attendance.setUserId(user.getId());
            attendance.setDate(LocalDate.now());
            attendanceRepository.save(attendance);
            responseDto.setMessage("successfully checked-in");
            return responseDto;
        }
        responseDto.setStatusCode(Constants.FAILED_CODE);
        responseDto.setStatus(Constants.FAILED);
        responseDto.setMessage("User already Checked In!");
        return responseDto;
    }
    public ResponseDto checkOut(){
        ResponseDto responseDto= new ResponseDto();
        Utility utility= new Utility();
        String userName= utility.getUserName();
        User user= userRepository.findByEmail(userName);
        Attendance attendance= attendanceRepository.findByUserIdAndDate(user.getId(), LocalDate.now()).orElse(null);
        if(attendance.getCheckIn()==null){
            responseDto.setStatus(Constants.FAILED);
            responseDto.setMessage("User hasn't checked in yet! please check in");
            responseDto.setStatusCode(Constants.FAILED_CODE);
            return responseDto;
        }
        if(attendance.getCheckOut()!=null){
            responseDto.setStatus(Constants.FAILED);
            responseDto.setMessage("User Already checked Out!");
            responseDto.setStatusCode(Constants.FAILED_CODE);
            return responseDto;
        }
        LocalTime current= LocalTime.now();
        LocalTime checkOutTime= current.plusHours(5).plusMinutes(30);
        attendance.setCheckOut(checkOutTime);
        attendanceRepository.save(attendance);
        responseDto.setMessage("Successfully checked-out");
        return responseDto;
    }
    public ResponseDto getAttendanceByUserId(PaginationResponse<Attendance> request){
        Utility utility= new Utility();
        String userName= utility.getUserName();
        User user= userRepository.findByEmail(userName);
        ResponseDto responseDto= new ResponseDto();
        if(request.getStartDate()!=null && request.getEndDate()!=null){
            Pageable pageable = PageRequest.of(request.getPage(),request.getSize());
            Page<Attendance> attendanceList =  attendanceRepository.findByUserIdAndDateBetween(request.getUserId(), request.getStartDate(), request.getEndDate(), pageable);
            request.setTotal(attendanceList.getTotalElements());
            request.setResult(attendanceList.getContent());
            responseDto.setData(request.getResult());
            responseDto.setMessage("Attendance");
            return responseDto;
        }
        Pageable pageable = PageRequest.of(request.getPage(),request.getSize());
        Page<Attendance> attendanceList =  attendanceRepository.findALLByUserId(request.getUserId(), pageable);
        request.setTotal(attendanceList.getTotalElements());
        request.setResult(attendanceList.getContent());
        responseDto.setData(request.getResult());
        responseDto.setMessage("Attendance");
        return responseDto;
    }
    public ResponseDto getAttendanceByAdmin(PaginationResponse<Attendance> request){
        Utility utility= new Utility();
        String userName= utility.getUserName();
        User user= userRepository.findByEmail(userName);
        ResponseDto responseDto= new ResponseDto();
        if(request.getStartDate()!=null && request.getEndDate()!=null){
            Pageable pageable = PageRequest.of(request.getPage(),request.getSize());
            Page<Attendance> attendanceList =  attendanceRepository.findAttendanceBetweenDates(request.getStartDate(), request.getEndDate(), pageable);
            request.setTotal(attendanceList.getTotalElements());
            request.setResult(attendanceList.getContent());
            responseDto.setData(request.getResult());
            responseDto.setMessage("Attendance");
            return responseDto;
        }
        Pageable pageable = PageRequest.of(request.getPage(),request.getSize());
        Page<Attendance> attendanceList =  attendanceRepository.findAllAttendances(pageable);
        request.setTotal(attendanceList.getTotalElements());
        request.setResult(attendanceList.getContent());
        responseDto.setData(request.getResult());
        responseDto.setMessage("Attendance");
        return responseDto;
    }
}
