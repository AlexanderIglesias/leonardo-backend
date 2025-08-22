package com.alphanet.products.leonardobackend.repository;

import com.alphanet.products.leonardobackend.dto.projection.DepartmentMetricProjection;
import com.alphanet.products.leonardobackend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("""
            SELECT d.departmentName as department, 
                   COALESCE(SUM(tc.totalApprentices), 0) as apprenticesCount 
            FROM Department d 
            LEFT JOIN d.trainingCenters tc 
            GROUP BY d.departmentName 
            ORDER BY apprenticesCount DESC
            """)
    List<DepartmentMetricProjection> getDepartmentMetrics();
}
