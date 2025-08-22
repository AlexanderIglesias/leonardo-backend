package com.alphanet.products.leonardobackend.repository;

import com.alphanet.products.leonardobackend.dto.projection.ApprenticeCountProjection;
import com.alphanet.products.leonardobackend.dto.projection.CenterMetricProjection;
import com.alphanet.products.leonardobackend.dto.projection.EnglishLevelProjection;
import com.alphanet.products.leonardobackend.dto.projection.GitHubUserProjection;
import com.alphanet.products.leonardobackend.dto.projection.RecommendedInstructorProjection;
import com.alphanet.products.leonardobackend.entity.TrainingCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingCenterRepository extends JpaRepository<TrainingCenter, Long> {

    @Query("""
            SELECT tc.centerName as centerName, 
                   d.departmentName as department, 
                   tc.totalApprentices as totalApprentices, 
                   tc.githubUsers as githubUsers, 
                   tc.englishB1B2 as englishB1B2,
                   tc.id as centerId
            FROM TrainingCenter tc 
            JOIN tc.department d 
            ORDER BY tc.totalApprentices DESC
            """)
    List<CenterMetricProjection> getCenterMetrics();

    @Query("SELECT COUNT(tc) FROM TrainingCenter tc")
    Long getTotalCentersCount();

    @Query("SELECT COALESCE(SUM(tc.totalApprentices), 0) FROM TrainingCenter tc")
    Long getTotalApprenticesCount();

    @Query("""
            SELECT COALESCE(AVG(CAST(tc.englishB1B2 AS double) / CAST(tc.totalApprentices AS double) * 100), 0) 
            FROM TrainingCenter tc WHERE tc.totalApprentices > 0
            """)
    Double getAverageEnglishPercentage();

    @Query("""
            SELECT tc.centerName as centerName, 
                   d.departmentName as department, 
                   tc.githubUsers as githubUsers, 
                   tc.totalApprentices as totalApprentices
            FROM TrainingCenter tc 
            JOIN tc.department d 
            ORDER BY tc.githubUsers DESC
            """)
    List<GitHubUserProjection> getGitHubUsersMetrics();

    @Query("""
            SELECT tc.centerName as centerName, 
                   d.departmentName as department, 
                   tc.englishB1B2 as englishB1B2, 
                   tc.totalApprentices as totalApprentices
            FROM TrainingCenter tc 
            JOIN tc.department d 
            ORDER BY tc.englishB1B2 DESC
            """)
    List<EnglishLevelProjection> getEnglishLevelMetrics();

    @Query("""
            SELECT tc.centerName as centerName, 
                   d.departmentName as department, 
                   tc.totalApprentices as totalApprentices
            FROM TrainingCenter tc 
            JOIN tc.department d 
            ORDER BY tc.totalApprentices DESC
            """)
    List<ApprenticeCountProjection> getApprenticeCountMetrics();

    @Query("""
            SELECT tc.centerName as centerName, 
                   d.departmentName as department, 
                   tc.id as centerId
            FROM TrainingCenter tc 
            JOIN tc.department d 
            ORDER BY tc.centerName ASC
            """)
    List<RecommendedInstructorProjection> getRecommendedInstructorMetrics();
}
