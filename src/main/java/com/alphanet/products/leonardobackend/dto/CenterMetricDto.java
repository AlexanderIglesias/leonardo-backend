package com.alphanet.products.leonardobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for training center metrics")
public class CenterMetricDto {

    @Schema(description = "Training center name", example = "SENA - Centro de Biotecnología Industrial")
    private String centerName;

    @Schema(description = "Department where the center is located", example = "Cundinamarca")
    private String department;

    @Schema(description = "Total number of apprentices in the center", example = "45")
    private Integer totalApprentices;

    @Schema(description = "List of recommended instructors", example = "[\"María García López\", \"Carlos Andrés Rodríguez\"]")
    private List<String> instructorsRecommended;

    @Schema(description = "Number of GitHub users", example = "32")
    private Integer githubUsers;

    @Schema(description = "Number of apprentices with B1-B2 English level", example = "28")
    private Integer englishB1B2;
}
