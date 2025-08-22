package com.alphanet.products.leonardobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for recommended instructors by training center")
public class RecommendedInstructorDto {

    @Schema(description = "Training center name", example = "SENA - Centro de Biotecnología Industrial")
    private String centerName;

    @Schema(description = "Department where the center is located", example = "Cundinamarca")
    private String department;

    @Schema(description = "List of recommended instructors", example = "[\"María García López\", \"Carlos Andrés Rodríguez\"]")
    private List<String> instructorsRecommended;

    @Schema(description = "Number of recommended instructors", example = "2")
    private Integer instructorsCount;
}
