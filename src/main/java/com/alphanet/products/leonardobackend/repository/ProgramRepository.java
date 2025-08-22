package com.alphanet.products.leonardobackend.repository;

import com.alphanet.products.leonardobackend.dto.projection.ProgramMetricProjection;
import com.alphanet.products.leonardobackend.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    @Query("""
            SELECT tc.centerName as centerName, 
                   p.programName as programName, 
                   p.apprenticesCount as apprenticesCount 
            FROM Program p 
            JOIN p.trainingCenter tc 
            ORDER BY p.apprenticesCount DESC
            """)
    List<ProgramMetricProjection> getProgramMetrics();

    @Query("""
            SELECT COUNT(DISTINCT p.programName) 
            FROM Program p 
            WHERE p.programName LIKE '%Backend%' 
               OR p.programName LIKE '%Desarrollo%'
               OR p.programName LIKE '%Software%'
            """)
    Long getBackendDevelopersCount();
}
