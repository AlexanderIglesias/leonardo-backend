package com.alphanet.products.leonardobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for apprentice count by training center")
public class ApprenticeCountDto {

    @Schema(description = "Training center name", example = "SENA - Centro de Biotecnología Industrial")
    private String centerName;

    @Schema(description = "Department where the center is located", example = "Cundinamarca")
    private String department;

    @Schema(description = "Total number of apprentices in the center", example = "45")
    private Integer totalApprentices;
}
