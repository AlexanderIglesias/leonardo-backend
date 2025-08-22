package com.alphanet.products.leonardobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for training program metrics")
public class ProgramMetricDto {

    @Schema(description = "Training center name", example = "SENA - Centro de Biotecnología Industrial")
    private String centerName;

    @Schema(description = "Training program name", example = "Análisis y Desarrollo de Software")
    private String programName;

    @Schema(description = "Number of apprentices in the program", example = "35")
    private Integer apprenticesCount;
}
