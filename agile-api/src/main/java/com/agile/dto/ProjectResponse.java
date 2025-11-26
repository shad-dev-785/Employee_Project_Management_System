package com.agile.dto;

import com.agile.model.Project;
import com.agile.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectResponse {
    private Project project;
    private List<User> usersInProject= new ArrayList<>();
}
