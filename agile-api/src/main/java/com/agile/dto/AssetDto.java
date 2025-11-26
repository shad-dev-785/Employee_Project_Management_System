package com.agile.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AssetDto {
    private String name;
    private String description;
    private MultipartFile document;
    private String link;
}
