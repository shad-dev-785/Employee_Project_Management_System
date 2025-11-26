package com.agile.dto;

import com.agile.Status;
import com.agile.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String attachedFile;

    private String attachedImage;

    private String commentType;

    private double used;

    private Long taskId;
}
