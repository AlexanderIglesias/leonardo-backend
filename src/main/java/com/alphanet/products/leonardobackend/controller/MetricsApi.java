package com.alphanet.products.leonardobackend.controller;

import com.alphanet.products.leonardobackend.dto.CenterMetricDto;
import com.alphanet.products.leonardobackend.dto.DepartmentMetricDto;
import com.alphanet.products.leonardobackend.dto.ProgramMetricDto;
import com.alphanet.products.leonardobackend.dto.ScalarMetricDto;
import com.alphanet.products.leonardobackend.service.MetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
@Tag(name = "SENASoft Metrics API v1", description = "Version 1 - API for obtaining metrics and statistics of SENA apprentices, training centers and programs")
public class MetricsApi {

    private final MetricsService metricsService;

    @GetMapping("/scalar")
    @Operation(
            summary = "Get scalar metrics",
            description = "Returns general metrics such as total apprentices, percentage of backend profiles, training centers and average English proficiency"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Scalar metrics list obtained successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ScalarMetricDto.class),
                            examples = @ExampleObject(
                                    name = "Scalar metrics example",
                                    value = """
                                            [
                                                {
                                                    "description": "# Aprendices inscritos únicos",
                                                    "value": 775
                                                },
                                                {
                                                    "description": "% de perfiles DEV Backend",
                                                    "value": "43.5%"
                                                }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ScalarMetricDto>> getScalarData() {
        return ResponseEntity.ok(metricsService.getScalarMetrics());
    }

    @GetMapping("/by-center")
    @Operation(
            summary = "Get metrics by training center",
            description = "Returns detailed metrics grouped by each SENA training center"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Training center metrics list obtained successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CenterMetricDto.class),
                            examples = @ExampleObject(
                                    name = "Training center metrics example",
                                    value = """
                                            [
                                                {
                                                    "centerName": "SENA - Centro de Biotecnología Industrial",
                                                    "department": "Cundinamarca",
                                                    "totalApprentices": 45,
                                                    "instructorsRecommended": ["María García López", "Carlos Andrés Rodríguez"],
                                                    "githubUsers": 32,
                                                    "englishB1B2": 28
                                                }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CenterMetricDto>> getMetricsByCenter() {
        return ResponseEntity.ok(metricsService.getCenterMetrics());
    }

    @GetMapping("/by-program")
    @Operation(
            summary = "Get metrics by training program",
            description = "Returns metrics grouped by training center and program"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Training program metrics list obtained successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProgramMetricDto.class),
                            examples = @ExampleObject(
                                    name = "Training program metrics example",
                                    value = """
                                            [
                                                {
                                                    "centerName": "SENA - Centro de Biotecnología Industrial",
                                                    "programName": "Análisis y Desarrollo de Software",
                                                    "apprenticesCount": 35
                                                }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProgramMetricDto>> getMetricsByProgram() {
        return ResponseEntity.ok(metricsService.getProgramMetrics());
    }

    @GetMapping("/by-department")
    @Operation(
            summary = "Get metrics by department",
            description = "Returns metrics grouped by Colombian departments"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Department metrics list obtained successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DepartmentMetricDto.class),
                            examples = @ExampleObject(
                                    name = "Department metrics example",
                                    value = """
                                            [
                                                {
                                                    "department": "Cundinamarca",
                                                    "apprenticesCount": 145
                                                }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<DepartmentMetricDto>> getMetricsByDepartment() {
        return ResponseEntity.ok(metricsService.getDepartmentMetrics());
    }
}
