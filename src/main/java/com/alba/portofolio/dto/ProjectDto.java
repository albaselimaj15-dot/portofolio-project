package com.alba.portofolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;


    @NotBlank(message = "Link is required")
    private String link;


    private String imageUrl;
    private List<Long> skillIds;

    @NotNull(message = "Category is required")
    private Long categoryId;
}