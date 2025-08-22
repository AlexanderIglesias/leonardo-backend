package com.alphanet.products.leonardobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for department metrics")
public class DepartmentMetricDto {

    @Schema(description = "Colombian department name", example = "Cundinamarca")
    private String department;

    @Schema(description = "Total number of apprentices in the department", example = "145")
    private Integer apprenticesCount;
}
