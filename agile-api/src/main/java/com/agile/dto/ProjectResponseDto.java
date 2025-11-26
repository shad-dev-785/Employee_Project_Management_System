package com.agile.dto;

import com.agile.model.Project;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
public class ProjectResponseDto {

    private Project project;

    @Column(name = "is_admin")
    boolean isAdmin;
}
