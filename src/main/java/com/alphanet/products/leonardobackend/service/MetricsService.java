package com.alphanet.products.leonardobackend.service;

import com.alphanet.products.leonardobackend.dto.ApprenticeCountDto;
import com.alphanet.products.leonardobackend.dto.CenterMetricDto;
import com.alphanet.products.leonardobackend.dto.DepartmentMetricDto;
import com.alphanet.products.leonardobackend.dto.EnglishLevelDto;
import com.alphanet.products.leonardobackend.dto.GitHubUserDto;
import com.alphanet.products.leonardobackend.dto.ProgramMetricDto;
import com.alphanet.products.leonardobackend.dto.RecommendedInstructorDto;
import com.alphanet.products.leonardobackend.dto.ScalarMetricDto;

import java.util.List;

public interface MetricsService {

    List<ScalarMetricDto> getScalarMetrics();

    List<CenterMetricDto> getCenterMetrics();

    List<ProgramMetricDto> getProgramMetrics();

    List<DepartmentMetricDto> getDepartmentMetrics();

    List<GitHubUserDto> getGitHubUsersMetrics();

    List<EnglishLevelDto> getEnglishLevelMetrics();

    List<ApprenticeCountDto> getApprenticeCountMetrics();

    List<RecommendedInstructorDto> getRecommendedInstructorMetrics();
}
