package com.alphanet.products.leonardobackend.controller;

import com.alphanet.products.leonardobackend.dto.ApprenticeCountDto;
import com.alphanet.products.leonardobackend.dto.CenterMetricDto;
import com.alphanet.products.leonardobackend.dto.DepartmentMetricDto;
import com.alphanet.products.leonardobackend.dto.EnglishLevelDto;
import com.alphanet.products.leonardobackend.dto.ErrorResponse;
import com.alphanet.products.leonardobackend.dto.GitHubUserDto;
import com.alphanet.products.leonardobackend.dto.ProgramMetricDto;
import com.alphanet.products.leonardobackend.dto.RecommendedInstructorDto;
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
            @ApiResponse(
                    responseCode = "400", 
                    description = "Bad request - Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Not found - Data not available",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", 
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
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
            @ApiResponse(
                    responseCode = "400", 
                    description = "Bad request - Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Not found - Data not available",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", 
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
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
            @ApiResponse(
                    responseCode = "400", 
                    description = "Bad request - Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Not found - Data not available",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", 
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
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
            @ApiResponse(
                    responseCode = "400", 
                    description = "Bad request - Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Not found - Data not available",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", 
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<DepartmentMetricDto>> getMetricsByDepartment() {
        return ResponseEntity.ok(metricsService.getDepartmentMetrics());
    }

    @GetMapping("/github-users")
    @Operation(
            summary = "Get GitHub users metrics by training center",
            description = "Returns metrics specifically focused on GitHub users per training center, including percentage of total apprentices"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "GitHub users metrics list obtained successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GitHubUserDto.class),
                            examples = @ExampleObject(
                                    name = "GitHub users metrics example",
                                    value = """
                                            [
                                                {
                                                    "centerName": "SENA - Centro de Biotecnología Industrial",
                                                    "department": "Cundinamarca",
                                                    "githubUsers": 32,
                                                    "githubPercentage": "71.1%"
                                                }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", 
                    description = "Bad request - Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Not found - Data not available",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", 
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<GitHubUserDto>> getGitHubUsersMetrics() {
        return ResponseEntity.ok(metricsService.getGitHubUsersMetrics());
    }

    @GetMapping("/english-level")
    @Operation(
            summary = "Get English level B1/B2 metrics by training center",
            description = "Returns metrics specifically focused on apprentices with B1/B2 English level per training center, including percentage of total apprentices"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "English level metrics list obtained successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EnglishLevelDto.class),
                            examples = @ExampleObject(
                                    name = "English level metrics example",
                                    value = """
                                            [
                                                {
                                                    "centerName": "SENA - Centro de Biotecnología Industrial",
                                                    "department": "Cundinamarca",
                                                    "englishB1B2": 28,
                                                    "englishPercentage": "62.2%"
                                                    }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", 
                    description = "Bad request - Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Not found - Data not available",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", 
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<EnglishLevelDto>> getEnglishLevelMetrics() {
        return ResponseEntity.ok(metricsService.getEnglishLevelMetrics());
    }

    @GetMapping("/apprentice-count")
    @Operation(
            summary = "Get apprentice count by training center",
            description = "Returns only the count of apprentices per training center, without additional metrics"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Apprentice count metrics list obtained successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApprenticeCountDto.class),
                            examples = @ExampleObject(
                                    name = "Apprentice count metrics example",
                                    value = """
                                            [
                                                {
                                                    "centerName": "SENA - Centro de Biotecnología Industrial",
                                                    "department": "Cundinamarca",
                                                    "totalApprentices": 45
                                                }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", 
                    description = "Bad request - Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Not found - Data not available",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", 
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<ApprenticeCountDto>> getApprenticeCountMetrics() {
        return ResponseEntity.ok(metricsService.getApprenticeCountMetrics());
    }

    @GetMapping("/recommended-instructors")
    @Operation(
            summary = "Get recommended instructors by training center",
            description = "Returns only the recommended instructors per training center, without additional metrics"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Recommended instructors metrics list obtained successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecommendedInstructorDto.class),
                            examples = @ExampleObject(
                                    name = "Recommended instructors metrics example",
                                    value = """
                                            [
                                                {
                                                    "centerName": "SENA - Centro de Biotecnología Industrial",
                                                    "department": "Cundinamarca",
                                                    "instructorsRecommended": [
                                                        "María García López",
                                                        "Carlos Andrés Rodríguez"
                                                    ],
                                                    "instructorsCount": 2
                                                }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", 
                    description = "Bad request - Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Not found - Data not available",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", 
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<RecommendedInstructorDto>> getRecommendedInstructorMetrics() {
        return ResponseEntity.ok(metricsService.getRecommendedInstructorMetrics());
    }
}
