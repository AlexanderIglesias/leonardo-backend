package com.alphanet.products.leonardobackend.service.impl;

import com.alphanet.products.leonardobackend.dto.ApprenticeCountDto;
import com.alphanet.products.leonardobackend.dto.CenterMetricDto;
import com.alphanet.products.leonardobackend.dto.DepartmentMetricDto;
import com.alphanet.products.leonardobackend.dto.EnglishLevelDto;
import com.alphanet.products.leonardobackend.dto.GitHubUserDto;
import com.alphanet.products.leonardobackend.dto.ProgramMetricDto;
import com.alphanet.products.leonardobackend.dto.RecommendedInstructorDto;
import com.alphanet.products.leonardobackend.dto.ScalarMetricDto;
import com.alphanet.products.leonardobackend.dto.projection.ApprenticeCountProjection;
import com.alphanet.products.leonardobackend.dto.projection.CenterMetricProjection;
import com.alphanet.products.leonardobackend.dto.projection.DepartmentMetricProjection;
import com.alphanet.products.leonardobackend.dto.projection.EnglishLevelProjection;
import com.alphanet.products.leonardobackend.dto.projection.GitHubUserProjection;
import com.alphanet.products.leonardobackend.dto.projection.ProgramMetricProjection;
import com.alphanet.products.leonardobackend.dto.projection.RecommendedInstructorProjection;
import com.alphanet.products.leonardobackend.repository.DepartmentRepository;
import com.alphanet.products.leonardobackend.repository.InstructorRepository;
import com.alphanet.products.leonardobackend.repository.ProgramRepository;
import com.alphanet.products.leonardobackend.repository.TrainingCenterRepository;
import com.alphanet.products.leonardobackend.service.MetricsService;
import com.alphanet.products.leonardobackend.service.mapper.MetricsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetricsServiceImpl implements MetricsService {

    private final TrainingCenterRepository trainingCenterRepository;
    private final DepartmentRepository departmentRepository;
    private final ProgramRepository programRepository;
    private final InstructorRepository instructorRepository;
    private final MetricsMapper metricsMapper;

    @Override
    public List<ScalarMetricDto> getScalarMetrics() {
        log.debug("Retrieving scalar metrics");
        Long totalApprentices = trainingCenterRepository.getTotalApprenticesCount();
        Long totalCenters = trainingCenterRepository.getTotalCentersCount();
        Double avgEnglishPercentage = trainingCenterRepository.getAverageEnglishPercentage();
        Long backendProfiles = programRepository.getBackendDevelopersCount();

        long apprenticesCount = totalApprentices != null ? totalApprentices : 0L;
        long centersCount = totalCenters != null ? totalCenters : 0L;
        long backendCount = backendProfiles != null ? backendProfiles : 0L;
        double englishAvg = avgEnglishPercentage != null ? avgEnglishPercentage : 0.0;

        return List.of(
                metricsMapper.toScalarMetric("# Aprendices inscritos únicos", apprenticesCount),
                metricsMapper.toScalarMetric("% de perfiles DEV Backend",
                        metricsMapper.formatPercentage(metricsMapper.calculatePercentage(backendCount, apprenticesCount))),
                metricsMapper.toScalarMetric("Total centros de formación", centersCount),
                metricsMapper.toScalarMetric("Promedio inglés B1-B2",
                        metricsMapper.formatPercentage(englishAvg))
        );
    }

    @Override
    public List<CenterMetricDto> getCenterMetrics() {
        log.debug("Retrieving center metrics with recommended instructors");

        List<CenterMetricProjection> centerData = trainingCenterRepository.getCenterMetrics();

        return centerData.stream()
                .map(this::buildCenterMetricDto)
                .collect(Collectors.toList());
    }

    private CenterMetricDto buildCenterMetricDto(CenterMetricProjection projection) {
        List<String> recommendedInstructors = instructorRepository
                .getRecommendedInstructorsByCenter(projection.getCenterId());
        return metricsMapper.toCenterMetricDto(projection, recommendedInstructors);
    }

    @Override
    public List<ProgramMetricDto> getProgramMetrics() {
        log.debug("Retrieving program metrics");
        List<ProgramMetricProjection> programData = programRepository.getProgramMetrics();
        return programData.stream()
                .map(metricsMapper::toProgramMetricDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentMetricDto> getDepartmentMetrics() {
        log.debug("Retrieving department metrics");
        List<DepartmentMetricProjection> departmentData = departmentRepository.getDepartmentMetrics();
        return departmentData.stream()
                .map(metricsMapper::toDepartmentMetricDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GitHubUserDto> getGitHubUsersMetrics() {
        log.debug("Retrieving GitHub users metrics");
        List<GitHubUserProjection> githubData = trainingCenterRepository.getGitHubUsersMetrics();
        return githubData.stream()
                .map(this::buildGitHubUserDto)
                .collect(Collectors.toList());
    }

    private GitHubUserDto buildGitHubUserDto(GitHubUserProjection projection) {
        int totalApprentices = projection.getTotalApprentices() != null ? projection.getTotalApprentices() : 0;
        int githubUsers = projection.getGithubUsers() != null ? projection.getGithubUsers() : 0;
        
        String percentage = totalApprentices > 0 
            ? metricsMapper.formatPercentage(metricsMapper.calculatePercentage(githubUsers, totalApprentices))
            : "0%";
            
        return new GitHubUserDto(
            projection.getCenterName(),
            projection.getDepartment(),
            githubUsers,
            percentage
        );
    }

    @Override
    public List<EnglishLevelDto> getEnglishLevelMetrics() {
        log.debug("Retrieving English level B1/B2 metrics");
        List<EnglishLevelProjection> englishData = trainingCenterRepository.getEnglishLevelMetrics();
        return englishData.stream()
                .map(this::buildEnglishLevelDto)
                .collect(Collectors.toList());
    }

    private EnglishLevelDto buildEnglishLevelDto(EnglishLevelProjection projection) {
        int totalApprentices = projection.getTotalApprentices() != null ? projection.getTotalApprentices() : 0;
        int englishB1B2 = projection.getEnglishB1B2() != null ? projection.getEnglishB1B2() : 0;
        
        String percentage = totalApprentices > 0 
            ? metricsMapper.formatPercentage(metricsMapper.calculatePercentage(englishB1B2, totalApprentices))
            : "0%";
            
        return new EnglishLevelDto(
            projection.getCenterName(),
            projection.getDepartment(),
            englishB1B2,
            percentage
        );
    }

    @Override
    public List<ApprenticeCountDto> getApprenticeCountMetrics() {
        log.debug("Retrieving apprentice count metrics by center");
        List<ApprenticeCountProjection> apprenticeData = trainingCenterRepository.getApprenticeCountMetrics();
        return apprenticeData.stream()
                .map(this::buildApprenticeCountDto)
                .collect(Collectors.toList());
    }

    private ApprenticeCountDto buildApprenticeCountDto(ApprenticeCountProjection projection) {
        return new ApprenticeCountDto(
            projection.getCenterName(),
            projection.getDepartment(),
            projection.getTotalApprentices()
        );
    }

    @Override
    public List<RecommendedInstructorDto> getRecommendedInstructorMetrics() {
        log.debug("Retrieving recommended instructor metrics by center");
        List<RecommendedInstructorProjection> instructorData = trainingCenterRepository.getRecommendedInstructorMetrics();
        return instructorData.stream()
                .map(this::buildRecommendedInstructorDto)
                .collect(Collectors.toList());
    }

    private RecommendedInstructorDto buildRecommendedInstructorDto(RecommendedInstructorProjection projection) {
        List<String> recommendedInstructors = instructorRepository
                .getRecommendedInstructorsByCenter(projection.getCenterId());
        return new RecommendedInstructorDto(
            projection.getCenterName(),
            projection.getDepartment(),
            recommendedInstructors,
            recommendedInstructors.size()
        );
    }
}
