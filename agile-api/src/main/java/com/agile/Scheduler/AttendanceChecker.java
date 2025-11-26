package com.agile.Scheduler;

import com.agile.constant.AttendanceStatus;
import com.agile.constant.LeaveStatus;
import com.agile.model.Attendance;
import com.agile.model.Leave;
import com.agile.model.User;
import com.agile.repository.AttendanceRepository;
import com.agile.repository.LeaveRepository;
import com.agile.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class AttendanceChecker {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;

    public AttendanceChecker(UserRepository userRepository,
                             AttendanceRepository attendanceRepository,
                             LeaveRepository leaveRepository) {
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.leaveRepository = leaveRepository;
    }

    @Scheduled(cron = "0 0 18 * * MON-FRI")
    public void checkAttendance(){
        List<User> allUsers= userRepository.findAll();
        List<User> usersWithoutCheckIn= new ArrayList<>();
        for(User u:allUsers){
            Attendance attendance= attendanceRepository.findByUserIdAndDate(u.getId(), LocalDate.now()).orElse(null);
            if(attendance==null){
                usersWithoutCheckIn.add(u);
            }
        }
        for(User u:usersWithoutCheckIn){
            Leave leave= leaveRepository.findByUserIdAndDate(u.getId(),LocalDate.now()).orElse(null);
            if(leave!=null){
                if(leave.getStatus().equals(LeaveStatus.APPROVED)){
                    Attendance attendance= new Attendance();
                    attendance.setUserId(u.getId());
                    attendance.setName(u.getFirstname());
                    attendance.setDate(LocalDate.now());
                    attendance.setStatus(AttendanceStatus.LEAVE);
                    attendanceRepository.save(attendance);
                }else{
                    Attendance attendance= new Attendance();
                    attendance.setUserId(u.getId());
                    attendance.setName(u.getFirstname());
                    attendance.setDate(LocalDate.now());
                    attendance.setStatus(AttendanceStatus.ABSENT);
                    attendanceRepository.save(attendance);
                }
            }else {
                Attendance attendance= new Attendance();
                attendance.setUserId(u.getId());
                attendance.setName(u.getFirstname());
                attendance.setDate(LocalDate.now());
                attendance.setStatus(AttendanceStatus.ABSENT);
                attendanceRepository.save(attendance);
            }
        }

    }
}
