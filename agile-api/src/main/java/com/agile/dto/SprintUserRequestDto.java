package com.agile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SprintUserRequestDto {
    private Long sprintId;
    private Long userId;
}
