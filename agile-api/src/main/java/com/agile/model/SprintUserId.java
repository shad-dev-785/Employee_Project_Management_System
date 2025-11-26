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
public class SprintUserId implements Serializable {
    @Column(name = "sprint_id",nullable = false)
    private Long sprintId;

    @Column(name = "user_id",nullable = false)
    private Long userId;
}
