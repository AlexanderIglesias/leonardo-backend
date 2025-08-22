package com.alphanet.products.leonardobackend.service.impl;

import com.alphanet.products.leonardobackend.dto.CenterMetricDto;
import com.alphanet.products.leonardobackend.dto.DepartmentMetricDto;
import com.alphanet.products.leonardobackend.dto.ProgramMetricDto;
import com.alphanet.products.leonardobackend.dto.ScalarMetricDto;
import com.alphanet.products.leonardobackend.dto.projection.CenterMetricProjection;
import com.alphanet.products.leonardobackend.dto.projection.DepartmentMetricProjection;
import com.alphanet.products.leonardobackend.dto.projection.ProgramMetricProjection;
import com.alphanet.products.leonardobackend.repository.DepartmentRepository;
import com.alphanet.products.leonardobackend.repository.InstructorRepository;
import com.alphanet.products.leonardobackend.repository.ProgramRepository;
import com.alphanet.products.leonardobackend.repository.TrainingCenterRepository;
import com.alphanet.products.leonardobackend.service.mapper.MetricsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("MetricsService Implementation Tests")
class MetricsServiceImplTest {

    @Mock
    private TrainingCenterRepository trainingCenterRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private MetricsMapper metricsMapper;

    @InjectMocks
    private MetricsServiceImpl metricsService;

    @BeforeEach
    void setUp() {
        // Setup common mock behaviors
    }

    @Test
    @DisplayName("Should return scalar metrics successfully")
    void shouldReturnScalarMetricsSuccessfully() {
        // Given
        when(trainingCenterRepository.getTotalApprenticesCount()).thenReturn(775L);
        when(trainingCenterRepository.getTotalCentersCount()).thenReturn(4L);
        when(trainingCenterRepository.getAverageEnglishPercentage()).thenReturn(57.3);
        when(programRepository.getBackendDevelopersCount()).thenReturn(338L);

        when(metricsMapper.toScalarMetric(any(String.class), any()))
                .thenAnswer(invocation -> new ScalarMetricDto(invocation.getArgument(0), invocation.getArgument(1)));
        when(metricsMapper.formatPercentage(any(Double.class))).thenReturn("43.5%");
        when(metricsMapper.calculatePercentage(anyLong(), anyLong())).thenReturn(43.5);

        // When
        List<ScalarMetricDto> result = metricsService.getScalarMetrics();

        // Then
        assertThat(result).hasSize(4);
        assertThat(result.get(0).getDescription()).isEqualTo("# Aprendices inscritos únicos");
        assertThat(result.get(0).getValue()).isEqualTo(775L);
    }

    @Test
    @DisplayName("Should handle null apprentices count gracefully")
    void shouldHandleNullApprenticesCountGracefully() {
        // Given
        when(trainingCenterRepository.getTotalApprenticesCount()).thenReturn(null);
        when(trainingCenterRepository.getTotalCentersCount()).thenReturn(4L);
        when(trainingCenterRepository.getAverageEnglishPercentage()).thenReturn(57.3);
        when(programRepository.getBackendDevelopersCount()).thenReturn(null);

        when(metricsMapper.toScalarMetric(any(String.class), any()))
                .thenAnswer(invocation -> new ScalarMetricDto(invocation.getArgument(0), invocation.getArgument(1)));
        when(metricsMapper.formatPercentage(any(Double.class))).thenReturn("0.0%");
        when(metricsMapper.calculatePercentage(anyLong(), anyLong())).thenReturn(0.0);

        // When
        List<ScalarMetricDto> result = metricsService.getScalarMetrics();

        // Then
        assertThat(result).hasSize(4);
        assertThat(result.get(0).getValue()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Should handle empty database gracefully")
    void shouldHandleEmptyDatabaseGracefully() {
        // Given
        when(trainingCenterRepository.getTotalApprenticesCount()).thenReturn(0L);
        when(trainingCenterRepository.getTotalCentersCount()).thenReturn(0L);
        when(trainingCenterRepository.getAverageEnglishPercentage()).thenReturn(0.0);
        when(programRepository.getBackendDevelopersCount()).thenReturn(0L);

        when(metricsMapper.toScalarMetric(any(String.class), any())).thenReturn(new ScalarMetricDto("test", 0));
        when(metricsMapper.formatPercentage(any(Double.class))).thenReturn("0.0%");
        when(metricsMapper.calculatePercentage(anyLong(), anyLong())).thenReturn(0.0);

        // When
        List<ScalarMetricDto> result = metricsService.getScalarMetrics();

        // Then
        assertThat(result).hasSize(4);
    }

    @Test
    @DisplayName("Should return center metrics with instructors")
    void shouldReturnCenterMetricsWithInstructors() {
        // Given
        CenterMetricProjection projection = new CenterMetricProjection() {
            @Override
            public Long getCenterId() {
                return 1L;
            }

            @Override
            public String getCenterName() {
                return "SENA - Centro de Biotecnología";
            }

            @Override
            public String getDepartment() {
                return "Cundinamarca";
            }

            @Override
            public Integer getTotalApprentices() {
                return 167;
            }

            @Override
            public Integer getGithubUsers() {
                return 120;
            }

            @Override
            public Integer getEnglishB1B2() {
                return 89;
            }
        };

        List<String> instructors = Arrays.asList("María García", "Carlos Rodríguez");
        CenterMetricDto expectedDto = new CenterMetricDto(
                "SENA - Centro de Biotecnología", "Cundinamarca", 167, instructors, 120, 89
        );

        when(trainingCenterRepository.getCenterMetrics()).thenReturn(List.of(projection));
        when(instructorRepository.getRecommendedInstructorsByCenter(1L)).thenReturn(instructors);
        when(metricsMapper.toCenterMetricDto(projection, instructors)).thenReturn(expectedDto);

        // When
        List<CenterMetricDto> result = metricsService.getCenterMetrics();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCenterName()).isEqualTo("SENA - Centro de Biotecnología");
        assertThat(result.get(0).getInstructorsRecommended()).hasSize(2);
    }

    @Test
    @DisplayName("Should handle empty center metrics")
    void shouldHandleEmptyCenterMetrics() {
        // Given
        when(trainingCenterRepository.getCenterMetrics()).thenReturn(Collections.emptyList());

        // When
        List<CenterMetricDto> result = metricsService.getCenterMetrics();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return program metrics successfully")
    void shouldReturnProgramMetricsSuccessfully() {
        // Given
        ProgramMetricProjection projection = new ProgramMetricProjection() {
            @Override
            public String getCenterName() {
                return "SENA - Centro de Biotecnología";
            }

            @Override
            public String getProgramName() {
                return "Análisis y Desarrollo de Software";
            }

            @Override
            public Integer getApprenticesCount() {
                return 85;
            }
        };

        ProgramMetricDto expectedDto = new ProgramMetricDto(
                "SENA - Centro de Biotecnología", "Análisis y Desarrollo de Software", 85
        );

        when(programRepository.getProgramMetrics()).thenReturn(List.of(projection));
        when(metricsMapper.toProgramMetricDto(projection)).thenReturn(expectedDto);

        // When
        List<ProgramMetricDto> result = metricsService.getProgramMetrics();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCenterName()).isEqualTo("SENA - Centro de Biotecnología");
        assertThat(result.get(0).getProgramName()).isEqualTo("Análisis y Desarrollo de Software");
        assertThat(result.get(0).getApprenticesCount()).isEqualTo(85);
    }

    @Test
    @DisplayName("Should return department metrics successfully")
    void shouldReturnDepartmentMetricsSuccessfully() {
        // Given
        DepartmentMetricProjection projection = new DepartmentMetricProjection() {
            @Override
            public String getDepartment() {
                return "Cundinamarca";
            }

            @Override
            public Long getApprenticesCount() {
                return 167L;
            }
        };

        DepartmentMetricDto expectedDto = new DepartmentMetricDto("Cundinamarca", 167);

        when(departmentRepository.getDepartmentMetrics()).thenReturn(List.of(projection));
        when(metricsMapper.toDepartmentMetricDto(projection)).thenReturn(expectedDto);

        // When
        List<DepartmentMetricDto> result = metricsService.getDepartmentMetrics();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDepartment()).isEqualTo("Cundinamarca");
        assertThat(result.get(0).getApprenticesCount()).isEqualTo(167);
    }

    @Test
    @DisplayName("Should handle empty program metrics")
    void shouldHandleEmptyProgramMetrics() {
        // Given
        when(programRepository.getProgramMetrics()).thenReturn(Collections.emptyList());

        // When
        List<ProgramMetricDto> result = metricsService.getProgramMetrics();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should handle empty department metrics")
    void shouldHandleEmptyDepartmentMetrics() {
        // Given
        when(departmentRepository.getDepartmentMetrics()).thenReturn(Collections.emptyList());

        // When
        List<DepartmentMetricDto> result = metricsService.getDepartmentMetrics();

        // Then
        assertThat(result).isEmpty();
    }
}
