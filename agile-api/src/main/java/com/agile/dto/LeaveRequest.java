package com.agile.dto;

import com.agile.constant.LeaveType;
import lombok.Data;

import java.time.LocalDate;
@Data
public class LeaveRequest {
private LeaveType leaveType;
private LocalDate startDate;
private LocalDate endDate;
private String approvedBy;
private int days;

}
