package com.alphanet.products.leonardobackend.service;

import com.alphanet.products.leonardobackend.dto.CenterMetricDto;
import com.alphanet.products.leonardobackend.dto.DepartmentMetricDto;
import com.alphanet.products.leonardobackend.dto.ProgramMetricDto;
import com.alphanet.products.leonardobackend.dto.ScalarMetricDto;

import java.util.List;

public interface MetricsService {

    List<ScalarMetricDto> getScalarMetrics();

    List<CenterMetricDto> getCenterMetrics();

    List<ProgramMetricDto> getProgramMetrics();

    List<DepartmentMetricDto> getDepartmentMetrics();
}
