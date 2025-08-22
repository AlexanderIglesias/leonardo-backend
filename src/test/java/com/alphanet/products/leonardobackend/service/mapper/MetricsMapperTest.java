package com.alphanet.products.leonardobackend.service.mapper;

import com.alphanet.products.leonardobackend.dto.CenterMetricDto;
import com.alphanet.products.leonardobackend.dto.DepartmentMetricDto;
import com.alphanet.products.leonardobackend.dto.ProgramMetricDto;
import com.alphanet.products.leonardobackend.dto.ScalarMetricDto;
import com.alphanet.products.leonardobackend.dto.projection.CenterMetricProjection;
import com.alphanet.products.leonardobackend.dto.projection.DepartmentMetricProjection;
import com.alphanet.products.leonardobackend.dto.projection.ProgramMetricProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MetricsMapper Tests")
class MetricsMapperTest {

    private MetricsMapper metricsMapper;

    @BeforeEach
    void setUp() {
        metricsMapper = new MetricsMapper();
    }

    @Test
    @DisplayName("Should create scalar metric with string value")
    void shouldCreateScalarMetricWithStringValue() {
        // Given
        String description = "Test Metric";
        String value = "43.5%";

        // When
        ScalarMetricDto result = metricsMapper.toScalarMetric(description, value);

        // Then
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create scalar metric with numeric value")
    void shouldCreateScalarMetricWithNumericValue() {
        // Given
        String description = "Total Count";
        Long value = 775L;

        // When
        ScalarMetricDto result = metricsMapper.toScalarMetric(description, value);

        // Then
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should handle null description gracefully")
    void shouldHandleNullDescriptionGracefully() {
        // Given
        String description = null;
        Long value = 100L;

        // When
        ScalarMetricDto result = metricsMapper.toScalarMetric(description, value);

        // Then
        assertThat(result.getDescription()).isNull();
        assertThat(result.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should handle null value gracefully")
    void shouldHandleNullValueGracefully() {
        // Given
        String description = "Test Metric";
        Object value = null;

        // When
        ScalarMetricDto result = metricsMapper.toScalarMetric(description, value);

        // Then
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getValue()).isNull();
    }

    @Test
    @DisplayName("Should calculate percentage correctly")
    void shouldCalculatePercentageCorrectly() {
        // Given
        long part = 338L;
        long total = 775L;

        // When
        double result = metricsMapper.calculatePercentage(part, total);

        // Then
        assertThat(result).isCloseTo(43.61, org.assertj.core.data.Offset.offset(0.01));
    }

    @Test
    @DisplayName("Should handle zero total in percentage calculation")
    void shouldHandleZeroTotalInPercentageCalculation() {
        // Given
        long part = 338L;
        long total = 0L;

        // When
        double result = metricsMapper.calculatePercentage(part, total);

        // Then
        assertThat(result).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should handle zero part in percentage calculation")
    void shouldHandleZeroPartInPercentageCalculation() {
        // Given
        long part = 0L;
        long total = 775L;

        // When
        double result = metricsMapper.calculatePercentage(part, total);

        // Then
        assertThat(result).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should handle negative values in percentage calculation")
    void shouldHandleNegativeValuesInPercentageCalculation() {
        // Given
        long part = -10L;
        long total = 100L;

        // When
        double result = metricsMapper.calculatePercentage(part, total);

        // Then
        assertThat(result).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should format percentage with one decimal place")
    void shouldFormatPercentageWithOneDecimalPlace() {
        // Given
        double percentage = 43.612;

        // When
        String result = metricsMapper.formatPercentage(percentage);

        // Then
        assertThat(result).isEqualTo("43.6%");
    }

    @Test
    @DisplayName("Should format zero percentage")
    void shouldFormatZeroPercentage() {
        // Given
        double percentage = 0.0;

        // When
        String result = metricsMapper.formatPercentage(percentage);

        // Then
        assertThat(result).isEqualTo("0.0%");
    }

    @Test
    @DisplayName("Should format very small percentage")
    void shouldFormatVerySmallPercentage() {
        // Given
        double percentage = 0.001;

        // When
        String result = metricsMapper.formatPercentage(percentage);

        // Then
        assertThat(result).isEqualTo("0.0%");
    }

    @Test
    @DisplayName("Should format 100 percentage")
    void shouldFormat100Percentage() {
        // Given
        double percentage = 100.0;

        // When
        String result = metricsMapper.formatPercentage(percentage);

        // Then
        assertThat(result).isEqualTo("100.0%");
    }

    @Test
    @DisplayName("Should handle NaN in percentage formatting")
    void shouldHandleNaNInPercentageFormatting() {
        // Given
        double percentage = Double.NaN;

        // When
        String result = metricsMapper.formatPercentage(percentage);

        // Then
        assertThat(result).isEqualTo("0.0%");
    }

    @Test
    @DisplayName("Should handle infinity in percentage formatting")
    void shouldHandleInfinityInPercentageFormatting() {
        // Given
        double percentage = Double.POSITIVE_INFINITY;

        // When
        String result = metricsMapper.formatPercentage(percentage);

        // Then
        assertThat(result).isEqualTo("0.0%");
    }

    @Test
    @DisplayName("Should map center metric projection to DTO")
    void shouldMapCenterMetricProjectionToDto() {
        // Given
        CenterMetricProjection projection = new CenterMetricProjection() {
            @Override
            public Long getCenterId() {
                return 1L;
            }

            @Override
            public String getCenterName() {
                return "SENA - Centro Test";
            }

            @Override
            public String getDepartment() {
                return "Test Department";
            }

            @Override
            public Integer getTotalApprentices() {
                return 100;
            }

            @Override
            public Integer getGithubUsers() {
                return 80;
            }

            @Override
            public Integer getEnglishB1B2() {
                return 60;
            }
        };
        List<String> instructors = Arrays.asList("Instructor 1", "Instructor 2");

        // When
        CenterMetricDto result = metricsMapper.toCenterMetricDto(projection, instructors);

        // Then
        assertThat(result.getCenterName()).isEqualTo("SENA - Centro Test");
        assertThat(result.getDepartment()).isEqualTo("Test Department");
        assertThat(result.getTotalApprentices()).isEqualTo(100);
        assertThat(result.getGithubUsers()).isEqualTo(80);
        assertThat(result.getEnglishB1B2()).isEqualTo(60);
        assertThat(result.getInstructorsRecommended()).hasSize(2);
    }

    @Test
    @DisplayName("Should handle empty instructors list")
    void shouldHandleEmptyInstructorsList() {
        // Given
        CenterMetricProjection projection = new CenterMetricProjection() {
            @Override
            public Long getCenterId() {
                return 1L;
            }

            @Override
            public String getCenterName() {
                return "SENA - Centro Test";
            }

            @Override
            public String getDepartment() {
                return "Test Department";
            }

            @Override
            public Integer getTotalApprentices() {
                return 100;
            }

            @Override
            public Integer getGithubUsers() {
                return 80;
            }

            @Override
            public Integer getEnglishB1B2() {
                return 60;
            }
        };
        List<String> instructors = Collections.emptyList();

        // When
        CenterMetricDto result = metricsMapper.toCenterMetricDto(projection, instructors);

        // Then
        assertThat(result.getInstructorsRecommended()).isEmpty();
    }

    @Test
    @DisplayName("Should map program metric projection to DTO")
    void shouldMapProgramMetricProjectionToDto() {
        // Given
        ProgramMetricProjection projection = new ProgramMetricProjection() {
            @Override
            public String getCenterName() {
                return "SENA - Centro Test";
            }

            @Override
            public String getProgramName() {
                return "Test Program";
            }

            @Override
            public Integer getApprenticesCount() {
                return 50;
            }
        };

        // When
        ProgramMetricDto result = metricsMapper.toProgramMetricDto(projection);

        // Then
        assertThat(result.getCenterName()).isEqualTo("SENA - Centro Test");
        assertThat(result.getProgramName()).isEqualTo("Test Program");
        assertThat(result.getApprenticesCount()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should map department metric projection to DTO")
    void shouldMapDepartmentMetricProjectionToDto() {
        // Given
        DepartmentMetricProjection projection = new DepartmentMetricProjection() {
            @Override
            public String getDepartment() {
                return "Test Department";
            }

            @Override
            public Long getApprenticesCount() {
                return 200L;
            }
        };

        // When
        DepartmentMetricDto result = metricsMapper.toDepartmentMetricDto(projection);

        // Then
        assertThat(result.getDepartment()).isEqualTo("Test Department");
        assertThat(result.getApprenticesCount()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should handle null projection values")
    void shouldHandleNullProjectionValues() {
        // Given
        CenterMetricProjection projection = new CenterMetricProjection() {
            @Override
            public Long getCenterId() {
                return null;
            }

            @Override
            public String getCenterName() {
                return null;
            }

            @Override
            public String getDepartment() {
                return null;
            }

            @Override
            public Integer getTotalApprentices() {
                return null;
            }

            @Override
            public Integer getGithubUsers() {
                return null;
            }

            @Override
            public Integer getEnglishB1B2() {
                return null;
            }
        };
        List<String> instructors = null;

        // When
        CenterMetricDto result = metricsMapper.toCenterMetricDto(projection, instructors);

        // Then
        assertThat(result.getCenterName()).isNull();
        assertThat(result.getDepartment()).isNull();
        assertThat(result.getTotalApprentices()).isNull();
        assertThat(result.getInstructorsRecommended()).isNull();
    }
}
