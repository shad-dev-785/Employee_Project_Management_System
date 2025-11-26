package com.agile.dto;

import com.agile.model.Sprint;
import com.agile.model.SprintUser;
import com.agile.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SprintWithUserResponse {
    private Sprint sprint;
    private List<User> assignees;
}
