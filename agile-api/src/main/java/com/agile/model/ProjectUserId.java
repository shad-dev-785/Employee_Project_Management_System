package com.agile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class ProjectUserId implements Serializable {
    @Column(name = "project_id",nullable = false)
    private Long projectId;

    @Column(name = "user_id",nullable = false)
    private Long userId;
}
