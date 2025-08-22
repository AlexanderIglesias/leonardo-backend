package com.alphanet.products.leonardobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for system scalar metrics")
public class ScalarMetricDto {

    @Schema(description = "Metric description", example = "# Aprendices inscritos únicos")
    private String description;

    @Schema(description = "Metric value (can be number or text)", example = "775")
    private Object value;
}
