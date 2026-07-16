package com.alba.portofolio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillDto {
    private Long id;

    @NotBlank(message = "Skill name is required")
    private String name;


    @NotBlank(message = "Level is required")
    private String level;
}
