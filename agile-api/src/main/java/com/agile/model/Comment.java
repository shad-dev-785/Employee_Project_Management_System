package com.agile.model;

import com.agile.Status;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

//    @Column(nullable = false)
//    private String owner;

    @Column(name = "attached_image")
    private String attachedFile;

    @Column(name = "attached_file")
    private String attachedImage;

    @Column(name = "comment_type")
    private String commentType;

    private double used;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
