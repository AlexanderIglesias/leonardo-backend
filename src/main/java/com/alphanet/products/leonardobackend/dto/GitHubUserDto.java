package com.alphanet.products.leonardobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for GitHub users metrics")
public class GitHubUserDto {

    @Schema(description = "Training center name", example = "SENA - Centro de Biotecnología Industrial")
    private String centerName;

    @Schema(description = "Department where the center is located", example = "Cundinamarca")
    private String department;

    @Schema(description = "Number of GitHub users", example = "32")
    private Integer githubUsers;

    @Schema(description = "Percentage of GitHub users from total apprentices", example = "71.1%")
    private String githubPercentage;
}
