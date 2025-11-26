package com.agile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false, name = "task_id")
    private Long taskId;

    @Column(nullable = false, name = "task_description")
    private String taskDescription;

    @Column(nullable = false, name = "comment_description")
    private String commentDescription;

    @Column(nullable = false)
    private double used;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
