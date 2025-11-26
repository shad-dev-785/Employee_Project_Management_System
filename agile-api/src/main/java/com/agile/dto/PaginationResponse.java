package com.agile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {
int page;
int size;
Long userId;
String userName;
LocalDate startDate;
LocalDate endDate;
Long total;
List<T> result;
}
