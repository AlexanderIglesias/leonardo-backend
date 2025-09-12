package com.alphanet.products.leonardobackend.repository;

import com.alphanet.products.leonardobackend.dto.projection.EquipmentProjection;
import com.alphanet.products.leonardobackend.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query("""
            SELECT e.id as id,
                   e.name as name,
                   e.type as type,
                   e.brand as brand,
                   CASE 
                       WHEN LENGTH(e.description) > 200 
                       THEN CONCAT(SUBSTRING(e.description, 1, 197), '...')
                       ELSE e.description 
                   END as description,
                   e.priceRange as priceRange
            FROM Equipment e
            WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(e.type) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(e.brand) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(e.description) LIKE LOWER(CONCAT('%', :query, '%'))
            ORDER BY e.relevanceScore DESC NULLS LAST, e.name ASC
            """)
    Page<EquipmentProjection> findRelevantEquipment(@Param("query") String query, Pageable pageable);

    @Query("""
            SELECT e.id as id,
                   e.name as name,
                   e.type as type,
                   e.brand as brand,
                   CASE 
                       WHEN LENGTH(e.description) > 200 
                       THEN CONCAT(SUBSTRING(e.description, 1, 197), '...')
                       ELSE e.description 
                   END as description,
                   e.priceRange as priceRange
            FROM Equipment e
            ORDER BY e.relevanceScore DESC NULLS LAST, e.name ASC
            """)
    Page<EquipmentProjection> findAllEquipmentProjections(Pageable pageable);

    @Query("""
            SELECT e.id as id,
                   e.name as name,
                   e.type as type,
                   e.brand as brand,
                   CASE 
                       WHEN LENGTH(e.description) > 200 
                       THEN CONCAT(SUBSTRING(e.description, 1, 197), '...')
                       ELSE e.description 
                   END as description,
                   e.priceRange as priceRange
            FROM Equipment e
            WHERE LOWER(e.type) = LOWER(:type)
            ORDER BY e.relevanceScore DESC NULLS LAST, e.name ASC
            """)
    Page<EquipmentProjection> findByTypeProjection(@Param("type") String type, Pageable pageable);
}