package com.agile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskWithCommentDto {
    private TaskRequestDto taskRequestDto;
    private CommentRequestDto commentRequestDto;
}
