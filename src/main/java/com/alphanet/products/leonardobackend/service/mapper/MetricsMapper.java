package com.alphanet.products.leonardobackend.service.mapper;

import com.alphanet.products.leonardobackend.dto.CenterMetricDto;
import com.alphanet.products.leonardobackend.dto.DepartmentMetricDto;
import com.alphanet.products.leonardobackend.dto.ProgramMetricDto;
import com.alphanet.products.leonardobackend.dto.ScalarMetricDto;
import com.alphanet.products.leonardobackend.dto.projection.CenterMetricProjection;
import com.alphanet.products.leonardobackend.dto.projection.DepartmentMetricProjection;
import com.alphanet.products.leonardobackend.dto.projection.ProgramMetricProjection;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Dedicated mapper component following SRP
 * Handles all projection -> DTO conversions
 */
@Component
public class MetricsMapper {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");

    public ScalarMetricDto toScalarMetric(String description, Object value) {
        return new ScalarMetricDto(description, value);
    }

    public CenterMetricDto toCenterMetricDto(CenterMetricProjection projection, List<String> instructors) {
        return new CenterMetricDto(
                projection.getCenterName(),
                projection.getDepartment(),
                projection.getTotalApprentices(),
                instructors,
                projection.getGithubUsers(),
                projection.getEnglishB1B2()
        );
    }

    public ProgramMetricDto toProgramMetricDto(ProgramMetricProjection projection) {
        return new ProgramMetricDto(
                projection.getCenterName(),
                projection.getProgramName(),
                projection.getApprenticesCount()
        );
    }

    public DepartmentMetricDto toDepartmentMetricDto(DepartmentMetricProjection projection) {
        return new DepartmentMetricDto(
                projection.getDepartment(),
                projection.getApprenticesCount().intValue()
        );
    }

    public String formatPercentage(double value) {
        // Handle edge cases: NaN, Infinity, etc.
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return "0.0%";
        }
        return DECIMAL_FORMAT.format(value) + "%";
    }

    public double calculatePercentage(long numerator, long denominator) {
        if (denominator <= 0 || numerator < 0) {
            return 0.0;
        }
        return (numerator * 100.0) / denominator;
    }
}
