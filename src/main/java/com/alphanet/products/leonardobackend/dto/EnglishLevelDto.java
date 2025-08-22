package com.alphanet.products.leonardobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for English level B1/B2 metrics")
public class EnglishLevelDto {

    @Schema(description = "Training center name", example = "SENA - Centro de Biotecnología Industrial")
    private String centerName;

    @Schema(description = "Department where the center is located", example = "Cundinamarca")
    private String department;

    @Schema(description = "Number of apprentices with B1-B2 English level", example = "28")
    private Integer englishB1B2;

    @Schema(description = "Percentage of B1-B2 English level from total apprentices", example = "62.2%")
    private String englishPercentage;
}
