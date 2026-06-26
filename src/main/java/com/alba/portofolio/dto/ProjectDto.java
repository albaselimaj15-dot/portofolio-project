package com.alba.portofolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long id;
    private String title;
    private String description;
    private String link;
    private String imageUrl;
    private List<Long>skillIds;
    private Long categoryId;
}
